package opengl;

import net.darktree.lt2d.graphics.ResizeableBuffer;
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

		ByteBuffer rwbuf = buffer.asByteBuffer();

		rwbuf.position(0);

		for( int i = 0; i < 100; i ++ ) {
			assertEquals(12, rwbuf.get());
			assertEquals(420, rwbuf.getInt());
			assertEquals(69.0f, rwbuf.getFloat());
		}

		buffer.print();
	}

}
