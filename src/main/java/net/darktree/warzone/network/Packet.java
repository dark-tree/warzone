package net.darktree.warzone.network;

import net.darktree.warzone.Registries;
import net.darktree.warzone.network.urp.PacketByteBuffer;
import net.darktree.warzone.util.ElementType;
import net.darktree.warzone.util.Registry;
import net.darktree.warzone.world.World;
import net.darktree.warzone.world.action.manager.ActionManager;

import java.nio.ByteBuffer;
import java.util.function.Consumer;

public abstract class Packet<T> {

	public final Packet.Type type;

	public Packet(Packet.Type type) {
		this.type = type;
	}

	/**
	 * Return a value for a packet listener registered
	 * using {@link Relay#setPacketListener(Type, Consumer)}
	 * this method is not guaranteed to be called
	 */
	public abstract T getListenerValue();

	/**
	 * Executed on the main thread after the packet is
	 * constructed and packet listeners fired, if the packet
	 * is fired locally this will be called immediately
	 */
	public void apply(PacketContext context) {

	}

	/**
	 * Use this method to acquire the packet buffer
	 * to be later send with {@link Relay#sendMessage(int, ByteBuffer)}
	 * or {@link Relay#broadcastMessage(int, ByteBuffer)}
	 */
	protected ByteBuffer getBuffer() {
		ByteBuffer buffer = PacketByteBuffer.getMessageBuffer();
		buffer.putInt(Registries.PACKETS.byValue(type).id());
		return buffer;
	}

	public final void broadcast() {
		UserGroup.broadcast(this, Relay.NULL_USER);
	}

	public final void broadcastExceptHost() {
		UserGroup.broadcast(this, UserGroup.instance.host);
	}

	public final void sendToHost() {
		UserGroup.sendToHost(this);
	}

	public final void sendToUser(int uid) {
		UserGroup.sendToUser(this, uid);
	}

	public final void send(World world) {
		ActionManager manager = world.getManager();

		if (!manager.isLocal()) {
			if (manager.getSide() == Side.CLIENT) {
				sendToHost();
			} else {
				broadcastExceptHost();
			}
		}
	}

	public static class Type extends ElementType<Type> {
		private final Type.Constructor constructor;

		public Type(Type.Constructor constructor) {
			this.constructor = constructor;
		}

		public Packet<?> create(ByteBuffer buffer, PacketContext context) {
			return constructor.create(buffer, context);
		}

		@Override
		public Registry<Type> getRegistry() {
			return Registries.PACKETS;
		}

		public interface Constructor {
			Packet<?> create(ByteBuffer buffer, PacketContext relay);
		}
	}

}
