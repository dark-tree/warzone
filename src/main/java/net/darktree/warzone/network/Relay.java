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

	public static final int TIMEOUT = 3;
	public static final int PORT = 9698;

	private final Side side;
	private final Socket socket;
	private final PacketWriter writer;
	private final PacketReader reader;
	private final Map<Packet.Type, Consumer<?>> interceptors = new HashMap<>();

	private Runnable listener;
	private int uid;
	private boolean open;

	public static Relay instance;

	public static void open(Side side, String hostname, Consumer<Relay> openCallback, Consumer<String> errorCallback) {
		if (instance != null) {
			Logger.error("Another relay is still open! Closing...");
			instance.close();
		}

		open(side, hostname, PORT, opened -> {
			instance = opened;
			openCallback.accept(instance);
		}, errorCallback);
	}

	private static void open(Side side, String hostname, int port, Consumer<Relay> openCallback, Consumer<String> errorCallback) {
		try {
			instance = new Relay(side, hostname, port);
			Timer timer = Util.runAsyncAfter(() -> {
				instance.close();
				errorCallback.accept("Timeout! Failed to open a connection after " + TIMEOUT + " seconds!");
			}, TIMEOUT * 1000);

			instance.setOpenListener(() -> {
				openCallback.accept(instance);
				timer.cancel();
			});

			instance.start();
		} catch (Exception e) {
			errorCallback.accept("Failed to open a connection!");
		}
	}

	private Relay(Side side, String hostname, int port) throws IOException {
		this.side = side;
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
			Packet<?> packet = null;
			Object result = null;

			try {
				try{
					packet = Registries.PACKETS.getElement(id).create(side, buffer);
				} catch (IndexOutOfBoundsException e) {
					Logger.error("Unknown game packet with id: " + id + " received!");
					return;
				}

				result = packet.getListenerValue();

				// run apply() on the main thread
				Main.runSynced(packet::apply);
			} catch (Exception e) {
				if (packet != null) {
					Logger.error("Exception was thrown while processing game packet: '" + Registries.PACKETS.keyOf(packet.type) + "'!");
				}

				e.printStackTrace();
			}

			@SuppressWarnings("unchecked")
			Consumer<Object> consumer = (Consumer<Object>) interceptors.get(packet.type);
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
				instance = null;

				// close the socket if there was a problem
				// while processing the packet
				if (socket.isConnected()) {
					this.close();
				}

				// FIXME cleanup after a connection drop, on close callback?
				e.printStackTrace();
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

	@Deprecated
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

	public <T> void setPacketListener(Packet.Type packet, Consumer<T> callback) {
		interceptors.put(packet, callback);
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

	@Override
	public String toString() {
		return side.name();
	}

}
