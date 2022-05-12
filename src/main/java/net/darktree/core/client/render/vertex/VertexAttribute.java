package net.darktree.core.client.render.vertex;

import org.lwjgl.opengl.GL32;

public class VertexAttribute {

	private final static int FLOAT = 4;
	public final int length;

	/**
	 * Helper for setting VAO attributes,
	 * learn more here: <a href="https://learnopengl.com/Getting-started/Hello-Triangle">https://learnopengl.com/Getting-started/Hello-Triangle</a>
	 *
	 * This method provides no way to normalize the data, or use types other than float
	 *
	 * @param index - id of this attribute
	 * @param length - number of components. must be 1, 2, 3, or 4
	 * @param stride - offset from the beginning of the vertex to the end of it, in bytes
	 * @param offset - offset from the beginning of the vertex to start of this attribute, in bytes
	 */
	public static void glVertexAttribute(int index, int length, int stride, int offset) {
		GL32.glVertexAttribPointer(index, length, GL32.GL_FLOAT, false, stride, offset);
		GL32.glEnableVertexAttribArray(index);
	}

	public VertexAttribute(int length) {
		this.length = length;
	}

	/**
	 * Apply this attribute to currently bound VAO
	 */
	public void apply(int index, int stride, int offset) {
		glVertexAttribute(index, this.length, stride * FLOAT, offset * FLOAT);
	}

}
