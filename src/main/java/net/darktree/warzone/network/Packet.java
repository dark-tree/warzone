package net.darktree.warzone.network;

import net.darktree.warzone.Registries;
import net.darktree.warzone.network.urp.PacketByteBuffer;

import java.nio.ByteBuffer;

public abstract class Packet<T> {

	public abstract T getListenerValue(Side side, ByteBuffer buffer);

	/**
	 * Use this method to acquire the packet buffer
	 * to be later send with {@link Relay#sendMessage(int, ByteBuffer)}
	 * or {@link Relay#broadcastMessage(ByteBuffer)}
	 */
	protected final ByteBuffer getBuffer() {
		ByteBuffer buffer = PacketByteBuffer.getMessageBuffer();
		buffer.putInt(Registries.PACKETS.identifierOf(this));
		return buffer;
	}

}
