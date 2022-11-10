package net.darktree.warzone.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class ByteBufferInputStream extends InputStream {

	private final ByteBuffer buffer;

	public ByteBufferInputStream(ByteBuffer buffer) {
		this.buffer = buffer;
	}

	@Override
	public int read() throws IOException {
		try {
			return buffer.get();
		} catch (Exception e) {
			throw new IOException(e);
		}
	}

}
