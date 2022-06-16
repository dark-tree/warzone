package net.darktree.core.network;

import net.darktree.core.Registries;
import net.darktree.core.network.urp.PacketReader;
import net.darktree.core.network.urp.PacketType;
import net.darktree.core.network.urp.PacketWriter;
import net.darktree.core.util.Logger;

import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;

public class Relay {

	private final Socket socket;
	private final PacketWriter writer;
	private final PacketReader reader;
	private boolean open;

	public Relay(String hostname, int port) throws IOException {
		socket = new Socket(hostname, port);

		writer = new PacketWriter(socket.getOutputStream());
		reader = new PacketReader(socket.getInputStream());

		reader.on(PacketType.R2U_WELC, buffer -> {
			int uid = buffer.getInt();
			int version = buffer.getInt();

			Logger.info("Connection with '" + hostname + ":" + port + "' established, as user #" + uid + ", using URP v" + version);

			if (version != 1) {
				throw new RuntimeException("Unsupported URP version: " + version);
			}

			open = true;
		});

		reader.on(PacketType.R2U_TEXT, buffer -> {
			final int id = buffer.getInt();

			try {
				Registries.PACKETS.getElement(id).onReceive(buffer);
			} catch (Exception e) {
				Logger.error("Unknown game packet with id: " + id + " received!");
			}
		});

		Thread thread = new Thread(() -> {
			try {
				while (true) {
					reader.next();
				}
			} catch (IOException e) {
				Logger.info("Connection closed");
				open = false;
			}
		});

		thread.setName("NetworkReaderThread");
		thread.start();
	}

	public boolean isOpen() {
		return open;
	}

	public void close() {
		try {
			// closing the socket will cause the reader thread
			// to terminate sooner or later by throwing an exception
			// when trying to read from a closed socket
			socket.close();
		} catch (Exception ignored) { }
	}

	public void createGroup() {
		writer.of(PacketType.U2R_MAKE).send();
	}

	public void joinGroup(int id) {
		writer.of(PacketType.U2R_JOIN).write(id).send();
	}

	public void sendMessage(int uid, ByteBuffer buffer) {
		writer.of(PacketType.U2R_SEND).write(uid).write(buffer.array());
	}

	public void broadcastMessage(ByteBuffer buffer) {
		writer.of(PacketType.U2R_BROD).write(buffer.array());
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
