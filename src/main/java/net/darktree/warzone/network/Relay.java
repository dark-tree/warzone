package net.darktree.warzone.network;

import net.darktree.warzone.Main;
import net.darktree.warzone.Registries;
import net.darktree.warzone.network.urp.PacketCallback;
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

	public static final int NULL_USER = 0;
	public static final int TIMEOUT = 4;
	public static final int PORT = 9698;

	private final Role role;
	private final Socket socket;
	private final PacketWriter writer;
	private final PacketReader reader;
	private final Map<Packet.Type, Consumer<?>> listeners = new HashMap<>();

	private State state;
	private int uid, gid;
	Consumer<Relay> onOpen;
	Consumer<String> onClose;

	/**
	 * Open a Relay and set a onClose callback, can return null if the socket failed to open
	 */
	public static Relay open(String hostname, int port, Role role, Consumer<String> onClose) {
		try {
			Relay relay = new Relay(hostname, port, role);
			relay.onClose(onClose);
			return relay;
		} catch (Exception exception) {
			onClose.accept("Failed to open socket! " + exception.getLocalizedMessage());
			return null;
		}
	}

	protected Relay(String hostname, int port, Role role) throws IOException {
		this.socket = new Socket(hostname, port);
		this.writer = new PacketWriter(socket.getOutputStream());
		this.reader = new PacketReader(socket.getInputStream());
		this.state = State.CONNECTING;
		this.role = role;

		Timer timer = Util.runAsyncAfter(() -> {
			if (state != State.CLOSED) close("Timeout! Failed to connect in " + TIMEOUT + " seconds!");
		}, TIMEOUT * 1000);

		on(PacketType.R2U_WELC, State.CONNECTING, buffer -> {
			this.uid = buffer.getInt();
			int version = buffer.getInt();

			Logger.info("Connection with '", hostname, ":", port, "' established, as user #", uid, ", using URP v", version);
			this.state = State.CONNECTED;
			this.role.onConnect(writer);

			if (version != 1) {
				close("Unsupported URP version: " + version);
			}
		});

		on(PacketType.R2U_MADE, State.CONNECTED, buffer -> {
			this.gid = buffer.getInt();

			Logger.info("Group #", gid, " joined!");
			this.state = State.READY;
			this.onOpen.accept(this);

			// cancel the timeout timer
			timer.cancel();
		});

		on(PacketType.R2U_TEXT, State.READY, buffer -> {
			final int id = buffer.getInt();
			Packet<?> packet = null;
			Object result = null;

			try {
				try{
					packet = Registries.PACKETS.getElement(id).create(buffer, role.side, this);
				} catch (IndexOutOfBoundsException e) {
					Logger.error("Unknown game packet with id: " + id + " received!");
					return;
				}

				result = packet.getListenerValue();

				// run apply() on the main thread
				Main.runSynced(packet::apply);
			} catch (Exception e) {
				if (packet != null) {
					Logger.error("Exception was thrown while processing game packet: '" + packet.type.key() + "'!");
				}

				e.printStackTrace();
			}

			@SuppressWarnings("unchecked")
			Consumer<Object> consumer = (Consumer<Object>) listeners.get(packet.type);
			if (consumer != null) {
				consumer.accept(result);
			}
		});
	}

	protected void on(PacketType type, State assertion, PacketCallback handler) {
		reader.on(type, packet -> {
			if (this.state != assertion) {
				Logger.warn("URP packet ", type, " was not expected now! Expected state was: ", assertion, ", but found: ", state, "!");
				return;
			}

			handler.call(packet);
		});
	}

	/**
	 * Close the connection with relay if open,
	 * this will also stop the worker thread
	 */
	public void close(String message) {
		Exception cause = null;

		// if we are in a group exit cleanly
		if (state == State.READY) {
			writer.of(PacketType.U2R_QUIT).send();
			writer.flush();
		}

		try {
			if (!socket.isClosed()) {
				socket.close();
			}
		} catch (Exception e) {
			cause = e;
		}

		if (socket.isClosed()) {
			this.state = State.CLOSED;
			onClose.accept(message);
		} else {
			throw new RuntimeException("Failed to close socket!", cause);
		}
	}

	/**
	 * Start the relay, must not be called more than
	 * once on the same object
	 */
	public void start() {
		Util.runAsync(() -> {
			try {
				// noinspection InfiniteLoopStatement
				while (true) reader.next();
			} catch (Throwable e) {
				Logger.info("Connection closed");

				// close the socket if there was a problem while processing the input
				if (!socket.isClosed() || this.state != State.CLOSED) {
					close("Error while processing input!");
					e.printStackTrace();
				}
			}
		}, "NetworkReaderThread");
	}

	public <T> void setPacketListener(Packet.Type packet, Consumer<T> callback) {
		listeners.put(packet, callback);
	}

	/**
	 * Send a message to one given user
	 */
	public void sendMessage(int uid, ByteBuffer buffer) {
		writer.of(PacketType.U2R_SEND).write(uid).write(buffer.array(), buffer.position()).send();
	}

	/**
	 * Send a message to all except the one given user
	 */
	public void broadcastMessage(int uid, ByteBuffer buffer) {
		writer.of(PacketType.U2R_BROD).write(uid).write(buffer.array(), buffer.position()).send();
	}

	/**
	 * Set a callback for a user joining your group
	 */
	public void onGroupJoined(IntCallback callback) {
		role.side.expect(Side.HOST);
		on(PacketType.R2U_JOIN, State.READY, buffer -> callback.call(buffer.getInt()));
	}

	/**
	 * Set a callback for a user leaving your group
	 */
	public void onGroupLeft(IntCallback callback) {
		role.side.expect(Side.HOST);
		on(PacketType.R2U_LEFT, State.READY, buffer -> callback.call(buffer.getInt()));
	}

	/**
	 * Set a callback for the relay being READY
	 */
	public void onOpen(Consumer<Relay> listener) {
		this.onOpen = listener;
	}

	/**
	 * Set a callback for the relay being CLOSED
	 */
	public void onClose(Consumer<String> listener) {
		this.onClose = listener;
	}

	/**
	 * Get the GID (group-identifier) of your user group
	 */
	public int getGid() {
		return gid;
	}

	/**
	 * Get the UID (user-identifier) of yourself
	 */
	public int getUid() {
		return uid;
	}

	@Override
	public String toString() {
		return role.side + " U=" + uid + " G=" + gid;
	}
}
