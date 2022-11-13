package net.darktree.warzone.network;

import net.darktree.warzone.Main;
import net.darktree.warzone.Registries;
import net.darktree.warzone.network.urp.PacketReader;
import net.darktree.warzone.network.urp.PacketType;
import net.darktree.warzone.network.urp.PacketWriter;
import net.darktree.warzone.util.Logger;
import net.darktree.warzone.util.Util;

import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.function.Consumer;

public class Relay {

	// the maximum join time
	public static final int TIMEOUT = 3;

	private final Socket socket;
	private final PacketWriter writer;
	private final PacketReader reader;
	private final Map<Packet<?>, Consumer<?>> interceptors = new HashMap<>();

	private Runnable listener;
	private int uid;
	private boolean open;

	private static Relay relay;

	public static void open(String hostname, Consumer<Relay> openCallback, Consumer<String> errorCallback) {
		if (relay != null) {
			if (relay.isOpen()) {
				Logger.warn("Another relay is still open! Closing...");
				relay.close();
			}

			relay = null;
			Main.relay = null;
		}

		open(hostname, 9698, opened -> {
			Main.relay = opened;
			relay = opened;
			openCallback.accept(relay);
		}, errorCallback);
	}

	private static void open(String hostname, int port, Consumer<Relay> openCallback, Consumer<String> errorCallback) {
		try {
			Relay.relay = new Relay(hostname, port);
			Timer timer = Util.runAsyncAfter(() -> {
				relay.close();
				errorCallback.accept("Timeout! Failed to open a connection after " + TIMEOUT + " seconds!");
			}, TIMEOUT * 1000);

			relay.setOpenListener(() -> {
				openCallback.accept(relay);
				timer.cancel();
			});

			relay.start();
		} catch (Exception e) {
			errorCallback.accept("Failed to open a connection!");
		}
	}

	private Relay(String hostname, int port) throws IOException {
		socket = new Socket(hostname, port);

		writer = new PacketWriter(socket.getOutputStream());
		reader = new PacketReader(socket.getInputStream());

		reader.on(PacketType.R2U_WELC, buffer -> {
			this.uid = buffer.getInt();
			int version = buffer.getInt();

			Logger.info("Connection with '" + hostname + ":" + port + "' established, as user #" + uid + ", using URP v" + version);

			if (version != 1) {
				throw new RuntimeException("Unsupported URP version: " + version);
			}

			listener.run();
			this.open = true;
		});

		reader.on(PacketType.R2U_TEXT, buffer -> {
			final int id = buffer.getInt();
			Packet<?> packet;
			Object result = null;

			try{
				packet = Registries.PACKETS.getElement(id);
			} catch (IndexOutOfBoundsException e) {
				Logger.error("Unknown game packet with id: " + id + " received!");
				return;
			}

			try {
				result = packet.onReceive(this, buffer);
			} catch (Exception e) {
				Logger.error("Exception was thrown while processing game packet with id: " + id + "!");
				e.printStackTrace();
			}

			@SuppressWarnings("unchecked")
			Consumer<Object> consumer = (Consumer<Object>) interceptors.get(packet);
			if (consumer != null) {
				consumer.accept(result);
			}
		});
	}

	private void start() {
		Util.runAsync(() -> {
			try {
				while (true) reader.next();
			} catch (Throwable e) {
				Logger.info("Connection closed");
				this.open = false;
			}
		}, "NetworkReaderThread");
	}

	public void close() {
		try {
			// closing the socket will cause the reader thread
			// to terminate sooner or later by throwing an exception
			// when trying to read from a closed socket
			socket.close();
		} catch (Exception ignored) { }
	}

	private void setOpenListener(Runnable listener) {
		this.listener = listener;
	}

	public boolean isOpen() {
		return open;
	}

	public int getUid() {
		return uid;
	}

	public void createGroup() {
		writer.of(PacketType.U2R_MAKE).send();
	}

	public void joinGroup(int id) {
		writer.of(PacketType.U2R_JOIN).write(id).send();
	}

	public void sendMessage(int uid, ByteBuffer buffer) {
		writer.of(PacketType.U2R_SEND).write(uid).write(buffer.array(), buffer.position()).send();
	}

	public <T> void setPacketListener(Packet<T> packet, Consumer<T> callback) {
		interceptors.put(packet, callback);
	}

	public <T> void removePacketListener(Packet<T> packet) {
		interceptors.remove(packet);
	}

	public void broadcastMessage(ByteBuffer buffer) {
		writer.of(PacketType.U2R_BROD).write(buffer.array(), buffer.position()).send();
	}

	public void onGroupCreated(IntCallback callback) {
		reader.on(PacketType.R2U_MADE, buffer -> callback.call(buffer.getInt()));
	}

	public void onGroupJoined(IntCallback callback) {
		reader.on(PacketType.R2U_JOIN, buffer -> callback.call(buffer.getInt()));
	}

	public void onGroupLeft(IntCallback callback) {
		reader.on(PacketType.R2U_LEFT, buffer -> callback.call(buffer.getInt()));
	}

}