package net.darktree.warzone.util;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class ByteBufferOutputStream extends OutputStream {

	private final ByteBuffer buffer;

	public ByteBufferOutputStream(ByteBuffer buffer) {
		this.buffer = buffer;
	}

	@Override
	public void write(int value) throws IOException {
		try {
			this.buffer.put((byte) value);
		} catch (Exception e) {
			throw new IOException(e);
		}
	}

}
