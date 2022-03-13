package net.darktree.opengl.vertex;

import net.darktree.opengl.ResizeableBuffer;
import org.lwjgl.opengl.GL32;

import java.util.ArrayList;
import java.util.List;

public class VertexBuffer implements AutoCloseable {

	public final int primitive, vertexSize;
	public final ResizeableBuffer buffer;

	int vao, vbo;
	boolean modified = false;

	public VertexBuffer(int primitive, int vertexSize) {
		this.primitive = primitive;
		this.vertexSize = vertexSize;
		this.buffer = new ResizeableBuffer(16);

		this.vao = GL32.glGenVertexArrays();
		this.vbo = GL32.glGenBuffers();

		GL32.glBindVertexArray(this.vao);
		GL32.glBindBuffer(GL32.GL_ARRAY_BUFFER, this.vbo);
	}

	public int count() {
		return this.buffer.position() / this.vertexSize;
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

	public VertexBuffer clear() {
		this.buffer.clear();
		this.modified = true;
		return this;
	}

	public static Builder create() {
		return create(Primitive.TRIANGLES);
	}

	public static Builder create(Primitive primitive) {
		return new Builder(primitive.type);
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

		private Builder(int primitive) {
			this.primitive = primitive;
		}

		private void apply() {
			int index = 0, offset = 0;

			for (VertexAttribute attribute : this.attributes) {
				attribute.apply(index, this.stride, offset);

				index ++;
				offset += attribute.length * attribute.type.size;
			}
		}

		/**
		 * Add vertex attribute
		 */
		public void attribute(VertexAttribute attribute) {
			this.attributes.add(attribute);

			this.stride += attribute.length * attribute.type.size;
		}

		/**
		 * Add simple vertex attribute
		 */
		public void attribute(int length, VertexAttribute.Type type) {
			this.attribute( new VertexAttribute(length, type) );
		}

		/**
		 * Add simple vertex float attribute
		 */
		public void attribute(int length) {
			this.attribute(length, VertexAttribute.Type.FLOAT);
		}

		public VertexBuffer build() {
			VertexBuffer buffer = new VertexBuffer(this.primitive, this.stride);
			this.apply();
			return buffer;
		}

	}

}
