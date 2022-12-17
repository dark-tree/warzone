package net.darktree.warzone.util;

import java.io.InputStream;
import java.nio.ByteBuffer;

public class ByteBufferInputStream extends InputStream {

	private final ByteBuffer buffer;

	public ByteBufferInputStream(ByteBuffer buffer) {
		this.buffer = buffer;
	}

	@Override
	public int read() {
		try {
			return buffer.get() & 0xff;
		} catch (Exception e) {
			return -1;
		}
	}

}
