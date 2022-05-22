package net.darktree.core.client.render;

import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;

public class ResizeableBuffer implements AutoCloseable {

	private ByteBuffer buffer;

	public ResizeableBuffer(int size) {
		if (size <= 0) {
			throw new IllegalArgumentException("Initial buffer size must be positive, got %s!".formatted(size));
		}

		this.buffer = MemoryUtil.memAlloc(size);
	}

	public ResizeableBuffer resize(int size) {
		this.buffer = MemoryUtil.memRealloc(this.buffer, size);
		return this;
	}

	public ResizeableBuffer reserve(int size) {
		int required = this.buffer.position() + size;
		if (required > this.buffer.capacity()) this.resize(required * 2);
		return this;
	}

	public ResizeableBuffer shrink() {
		return resize(this.buffer.position());
	}

	public void putFloat(float value) {
		this.buffer.putFloat(value);
	}

	public void putFloats(float... values) {
		for (float value : values) {
			this.buffer.putFloat(value);
		}
	}

	public void putInt(int value) {
		this.buffer.putInt(value);
	}

	public void putByte(byte value) {
		this.buffer.put(value);
	}

	public void clear() {
		this.buffer.clear();
	}

	public int position() {
		return this.buffer.position();
	}

	public long address() {
		return MemoryUtil.memAddress0(this.buffer);
	}

	@Override
	public void close() {
		MemoryUtil.memFree(this.buffer);
	}

	@Deprecated(forRemoval = true)
	public void print() {
		System.out.println(this.position() + " - pos");

		for( int i = 0; i < this.position(); i ++ ) {
			System.out.print(this.buffer.get(i) + " ");
		}
	}

	public ByteBuffer asByteBuffer() {
		return this.buffer;
	}
}
