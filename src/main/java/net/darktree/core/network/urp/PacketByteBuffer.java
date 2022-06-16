package net.darktree.core.network.urp;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class PacketByteBuffer {

	private static final int UINT32_MAX = 65535;
	public static final int DATA_MAX = UINT32_MAX - 4;
	public static final int PACKET_MAX = UINT32_MAX + 3;

	private static final ByteBuffer data = allocateOrderedBuffer(DATA_MAX);
	private static final ByteBuffer packet = allocateOrderedBuffer(PACKET_MAX);

	private static ByteBuffer allocateOrderedBuffer(int length) {
		ByteBuffer buffer = ByteBuffer.allocate(PACKET_MAX);
		buffer.order(ByteOrder.nativeOrder());
		return buffer;
	}

	public static ByteBuffer getMessageBuffer() {
		return data.rewind();
	}

	public static ByteBuffer getPacketBuffer() {
		return packet.rewind();
	}

}
