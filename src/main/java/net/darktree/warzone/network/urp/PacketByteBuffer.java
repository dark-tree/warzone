package net.darktree.warzone.network.urp;

import net.darktree.warzone.network.PacketBuffer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class PacketByteBuffer {

	private static final int UINT32_MAX = 65535;
	public static final int DATA_MAX = UINT32_MAX - 8;
	public static final int PACKET_MAX = UINT32_MAX + 3;

	private static final PacketBuffer data = allocateOrderedBuffer(DATA_MAX);
	private static final PacketBuffer packet = allocateOrderedBuffer(PACKET_MAX);

	private static PacketBuffer allocateOrderedBuffer(int length) {
		ByteBuffer buffer = ByteBuffer.allocate(length);
		buffer.order(ByteOrder.nativeOrder());
		return new PacketBuffer(buffer);
	}

	public static PacketBuffer getMessageBuffer() {
		data.buffer().rewind();
		return data;
	}

	public static PacketBuffer getPacketBuffer() {
		packet.buffer().rewind();
		return packet;
	}

}
