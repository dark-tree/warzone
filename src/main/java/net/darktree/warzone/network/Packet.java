package net.darktree.warzone.network;

import net.darktree.warzone.Registries;
import net.darktree.warzone.network.urp.PacketByteBuffer;
import net.darktree.warzone.util.ElementType;
import net.darktree.warzone.util.Registry;

import java.nio.ByteBuffer;

public abstract class Packet<T> {

	public final Packet.Type type;

	public Packet(Packet.Type type) {
		this.type = type;
	}

	public abstract T getListenerValue();

	/**
	 * Use this method to acquire the packet buffer
	 * to be later send with {@link Relay#sendMessage(int, ByteBuffer)}
	 * or {@link Relay#broadcastMessage(ByteBuffer)}
	 */
	protected ByteBuffer getBuffer() {
		ByteBuffer buffer = PacketByteBuffer.getMessageBuffer();
		buffer.putInt(Registries.PACKETS.identifierOf(type));
		return buffer;
	}

	public final void broadcast() {
		new PacketDelegate(getBuffer()).broadcast();
	}

	public final void sendToHost() {
		new PacketDelegate(getBuffer()).sendToHost();
	}

	public final void sendToUser(int uid) {
		new PacketDelegate(getBuffer()).sendToUser(uid);
	}

	public static class Type extends ElementType<Type> {
		private final Type.Constructor constructor;

		public Type(Type.Constructor constructor) {
			this.constructor = constructor;
		}

		public Packet<?> create(Side side, ByteBuffer buffer) {
			return constructor.create(side, buffer);
		}

		@Override
		public Registry<Type> getRegistry() {
			return Registries.PACKETS;
		}

		public interface Constructor {
			Packet<?> create(Side side, ByteBuffer buffer);
		}
	}

}
