package net.darktree.warzone.client.render.vertex;

import net.darktree.warzone.client.render.ResizeableBuffer;
import org.lwjgl.opengl.GL32;

import java.util.ArrayList;
import java.util.List;

public class VertexBuffer implements AutoCloseable {

	protected final int primitive, vertexSize;
	protected final ResizeableBuffer buffer;
	protected final boolean immediate;

	private int vao, vbo;
	private	boolean modified = false;

	public VertexBuffer(int primitive, int vertexSize, boolean immediate) {
		this.primitive = primitive;
		this.vertexSize = vertexSize;
		this.buffer = new ResizeableBuffer(16);
		this.immediate = immediate;

		this.vao = GL32.glGenVertexArrays();
		this.vbo = GL32.glGenBuffers();

		GL32.glBindVertexArray(this.vao);
		GL32.glBindBuffer(GL32.GL_ARRAY_BUFFER, this.vbo);
	}

	public int count() {
		return this.buffer.position() / this.vertexSize;
	}

	public boolean isEmpty() {
		return this.buffer.position() == 0;
	}

	public void bind() {
		GL32.glBindVertexArray(this.vao);

		// update if something was written to vertex buffer
		if (this.modified) {
			this.modified = false;

			GL32.glBindBuffer(GL32.GL_ARRAY_BUFFER, vbo);
			GL32.nglBufferData(GL32.GL_ARRAY_BUFFER, this.buffer.position(), this.buffer.address(), GL32.GL_DYNAMIC_DRAW);
		}
	}

	public void draw() {
		GL32.glDrawArrays(this.primitive, 0, this.count());
		if (immediate) clear();
	}

	public VertexBuffer putByte(byte value) {
		this.buffer.reserve(1).putByte(value);
		this.modified = true;
		return this;
	}

	public VertexBuffer putInt(int value) {
		this.buffer.reserve(4).putInt(value);
		this.modified = true;
		return this;
	}

	public VertexBuffer putFloat(float value) {
		this.buffer.reserve(4).putFloat(value);
		this.modified = true;
		return this;
	}

	public void clear() {
		this.buffer.clear();
		this.modified = true;
	}

	public static Builder create() {
		return new Builder();
	}

	@Override
	public void close() {
		if (this.vao != 0 && this.vbo != 0) {
			GL32.glDeleteVertexArrays(this.vao);
			GL32.glDeleteBuffers(this.vbo);

			this.vao = 0;
			this.vbo = 0;
		}
	}

	public static class Builder {

		final int primitive;
		final List<VertexAttribute> attributes = new ArrayList<>();

		int stride = 0;
		boolean immediate = false;

		private Builder() {
			this.primitive = GL32.GL_TRIANGLES;
		}

		private void apply() {
			int index = 0, offset = 0;

			for (VertexAttribute attribute : this.attributes) {
				attribute.apply(index, this.stride, offset);

				index ++;
				offset += attribute.length;
			}
		}

		/**
		 * Add vertex attribute
		 */
		public Builder attribute(VertexAttribute attribute) {
			this.attributes.add(attribute);
			this.stride += attribute.length;
			return this;
		}

		/**
		 * Add simple float vertex attribute
		 */
		public Builder attribute(int length) {
			return this.attribute(new VertexAttribute(length));
		}

		/**
		 * Add simple named float vertex attribute
		 */
		public Builder attribute(String name) {
			return this.attribute(name.length());
		}

		/**
		 * Make an immediate-like buffer (clear after use)
		 */
		public Builder immediate() {
			immediate = true;
			return this;
		}

		public VertexBuffer build() {
			VertexBuffer buffer = new VertexBuffer(this.primitive, this.stride, this.immediate);
			this.apply();
			return buffer;
		}

	}

}
