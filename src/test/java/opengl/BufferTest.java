package opengl;

import net.darktree.warzone.client.render.ResizeableBuffer;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BufferTest {

	@Test
	public void writeAndRead() {
		ResizeableBuffer buffer = new ResizeableBuffer(16);

		for( int i = 0; i < 100; i ++ ) {
			buffer.reserve(1).putByte((byte) 12);
			buffer.reserve(4).putInt(420);
			buffer.reserve(4).putFloat(69.0f);
		}

		ByteBuffer buf = buffer.asByteBuffer();

		buf.position(0);

		for( int i = 0; i < 100; i ++ ) {
			assertEquals(12, buf.get());
			assertEquals(420, buf.getInt());
			assertEquals(69.0f, buf.getFloat());
		}

		buffer.close();
	}

}
