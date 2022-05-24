package opengl;

import net.darktree.core.client.render.image.Atlas;
import net.darktree.core.client.render.image.Image;
import org.junit.jupiter.api.Test;
import org.lwjgl.system.MemoryUtil;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

public class ImageTest {

	@Test
	public void writeAndRead() {
		Image image = new Image(16, 16, Image.Format.RGBA);

		MemoryUtil.memPutInt(image.getPointer(0, 0), 420);
		MemoryUtil.memPutInt(image.getPointer(13, 4), 840);
		MemoryUtil.memPutInt(image.getPointer(15, 15), 0);
		MemoryUtil.memPutInt(image.getPointer(1, 2), 0x00FF00FF);
		MemoryUtil.memPutInt(image.getPointer(3, 1), 0x123456FF);

		assertEquals(420, MemoryUtil.memGetInt(image.getPointer(0, 0)));
		assertEquals(840, MemoryUtil.memGetInt(image.getPointer(13, 4)));
		assertEquals(0, MemoryUtil.memGetInt(image.getPointer(15, 15)));
		assertEquals(0x00FF00FF, MemoryUtil.memGetInt(image.getPointer(1, 2)));
		assertEquals(0x123456FF, MemoryUtil.memGetInt(image.getPointer(3, 1)));

		image.close();
	}

	@Test
	public void writeCopyAndRead() {
		Image t1 = new Image(16, 16, Image.Format.RGBA);

		for( int x = 0; x < 16; x ++ ) {
			for( int y = 0; y < 16; y ++ ) {
				MemoryUtil.memPutInt(t1.getPointer(x, y), (x + y * 33) << 8 | 0xFF);
			}
		}

		Image t2 = t1.copy(32, 32);
		t1.close();

		for( int x = 0; x < 16; x ++ ) {
			for( int y = 0; y < 16; y ++ ) {
				assertEquals(MemoryUtil.memGetInt(t2.getPointer(x, y)), ((x + y * 33) << 8 | 0xFF));
			}
		}
	}

	@Test
	public void loadFileAndRead() {
		Image image = Image.of("test.png", Image.Format.RGBA);

		assertEquals(image.format, Image.Format.RGBA);
		assertEquals(0xFF00FF00, MemoryUtil.memGetInt(image.getPointer(1, 2)));
		assertEquals(0x00000000, MemoryUtil.memGetInt(image.getPointer(3, 1)));
		assertEquals(0xFFFFFFFF, MemoryUtil.memGetInt(image.getPointer(6, 1)));
		assertEquals(0xFF000000, MemoryUtil.memGetInt(image.getPointer(4, 3)));
		assertEquals(0xFF563412, MemoryUtil.memGetInt(image.getPointer(1, 5)));
		assertEquals(0xFFFF4500, MemoryUtil.memGetInt(image.getPointer(4, 6)));

		image.close();
	}

}
