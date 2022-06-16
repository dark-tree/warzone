package net.darktree.core.network.urp;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class PacketByteBuffer {

	private static final int UINT32_MAX = 65535;
	public static final int DATA_MAX = UINT32_MAX - 4;
	public static final int PACKET_MAX = UINT32_MAX + 3;

	private static final ByteBuffer buffer = ByteBuffer.allocate(PACKET_MAX);

	static {
		buffer.order(ByteOrder.nativeOrder());
	}

	public static ByteBuffer getInstance() {
		return buffer.rewind();
	}

}
