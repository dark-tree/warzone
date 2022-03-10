package net.darktree.opengl.image;

import net.darktree.Resources;
import org.lwjgl.opengl.GL32;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class Image implements AutoCloseable {

	public final Format format;
	private final long size;
	private final int width;
	private final int height;
	private final boolean stb;

	private long buffer;

	protected Image(int width, int height, Format format, long buffer, boolean stb) {
		if (width < 0 || height < 0) {
			throw new RuntimeException("Invalid texture size, (%sx%s)!".formatted(width, height));
		}

		this.width = width;
		this.height = height;
		this.size = width * height * format.channels;
		this.buffer = buffer;
		this.stb = stb;
		this.format = format;
	}

	public long getOffset(int x, int y) {
		return (x + (long) y * this.width) * this.format.channels;
	}

	public long getPointer(int x, int y) {
		return this.buffer + getOffset(x, y);
	}

	public boolean checkBounds(int x, int y) {
		return x >= 0 && y >= 0 && x < this.width && y < this.height;
	}

	public void assertBounds(int x, int y) {
		if (!checkBounds(x, y)) {
			throw new ArrayIndexOutOfBoundsException("(%sx%s) is out of bounds for texture (%sx%s)!".formatted(x, y, width, height));
		}
	}

	public Image copy(int width, int height) {
		if (width < this.width || height < this.height) {
			throw new RuntimeException("Unable to make smaller copy!");
		}

		Image texture = of(width, height, this.format);
		texture.write(this, 0, 0);
		return texture;
	}

	public void write(Image source, int x, int y) {
		this.write(source, 0, 0, x, y, source.width, source.height);
	}

	public void write(Image source, int fx, int fy, int tx, int ty, int w, int h) {
		if (source.format != this.format) {
			throw new RuntimeException("Format mismatch!");
		}

		this.assertBounds(tx + w - 1, ty + h - 1);

		if (this.width == source.width && this.width == w) {
			MemoryUtil.memCopy(source.getPointer(fx, fy), getPointer(tx, ty), getOffset(w - 1, h - 1));
		}else{
			for (int y = 0; y < h; y ++) {
				MemoryUtil.memCopy(source.getPointer(fx, fy + y), getPointer(tx, ty + y), getOffset(w, 0));
			}
		}
	}

	public static Image of(int width, int height, Format format) {
		Image texture = new Image(width, height, format, 0, false);
		texture.buffer = MemoryUtil.nmemAlloc(texture.size);
		return texture;
	}

	public static Image of(String path, Format format) {
		ByteBuffer image;
		int width, height;

		try (MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer w = stack.mallocInt(1);
			IntBuffer h = stack.mallocInt(1);
			IntBuffer n = stack.mallocInt(1);

			image = STBImage.stbi_load(Resources.location(path).toString(), w, h, n, (int) format.channels);
			if (image == null) {
				throw new RuntimeException("Failed to load texture, " + STBImage.stbi_failure_reason() + "!");
			}

			width = w.get();
			height = h.get();
		}

		try {
			return new Image(width, height, format, MemoryUtil.memAddress(image), true);
		} catch (Exception e) {
			STBImage.stbi_image_free(image);
			throw e;
		}
	}

	public Texture asTexture(boolean useMipmaps) {
		return new Texture(this, useMipmaps);
	}

	@Override
	public void close() {
		if (this.buffer != 0) {
			if (this.stb) {
				STBImage.nstbi_image_free(this.buffer);
			} else {
				MemoryUtil.nmemFree(this.buffer);
			}
		}

		this.buffer = 0;
	}

	public int width() {
		return width;
	}

	public int height() {
		return height;
	}

	public enum Format {
		RGB(3, GL32.GL_RGB),
		RGBA(4, GL32.GL_RGBA);

		public final long channels;
		public final int glFormat;

		Format(long channels, int glFormat) {
			this.channels = channels;
			this.glFormat = glFormat;
		}
	}

}
