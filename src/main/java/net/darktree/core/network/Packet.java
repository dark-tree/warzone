package net.darktree.core.network;

import net.darktree.core.Registries;
import net.darktree.core.network.urp.PacketByteBuffer;

import java.nio.ByteBuffer;

public abstract class Packet {

	public void onReceive(ByteBuffer buffer) {

	}

	/**
	 * Use this method to acquire the packet buffer
	 * to be later send with {@link Relay#sendMessage(int, ByteBuffer)}
	 * or {@link Relay#broadcastMessage(ByteBuffer)}
	 */
	protected final ByteBuffer getBuffer() {
		ByteBuffer buffer = PacketByteBuffer.getInstance();
		buffer.putInt(Registries.PACKETS.identifierOf(this));
		return buffer;
	}

}
