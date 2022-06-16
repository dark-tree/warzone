package net.darktree.core.network.urp;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class PacketSender {

	private final OutputStream stream;
	private final ByteBuffer buffer = PacketByteBuffer.getInstance();

	public PacketSender(OutputStream stream, int type) {
		this.stream = stream;
		buffer.put((byte) (type & 0xFF));
		buffer.put((byte) 0);
		buffer.put((byte) 0);
	}

	public PacketSender(OutputStream stream, PacketType type) {
		this(stream, type.value);
	}

	public PacketSender write(int value) {
		buffer.putInt(value);
		return this;
	}

	public PacketSender write(byte[] bytes) {
		buffer.put(bytes);
		return this;
	}

	public void send() {
		short size = (short) (buffer.position() - 3);

		buffer.put(1, (byte) (size & 0xFF));
		buffer.put(2, (byte) ((size << 8) & 0xFF));

		try {
			stream.write(buffer.array(), 0, size + 3);
		} catch (IOException ignore) {

		}
	}

}
