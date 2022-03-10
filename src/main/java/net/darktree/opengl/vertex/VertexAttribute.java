package net.darktree.opengl.vertex;

import org.lwjgl.opengl.GL32;

public class VertexAttribute {

	public final int length;
	public final Type type;

	/**
	 * Helper for setting VAO attributes,
	 * learn more here: https://learnopengl.com/Getting-started/Hello-Triangle
	 *
	 * This method provides no way to normalize the data
	 *
	 * @param index - id of this attribute
	 * @param length - number of components. must be 1, 2, 3, or 4
	 * @param type - the type of data in each component
	 * @param stride - offset from the beginning of the vertex to the end of it, in bytes
	 * @param offset - offset from the beginning of the vertex to start of this attribute, in bytes
	 */
	public static void glVertexAttribute(int index, int length, Type type, int stride, int offset) {
		GL32.glVertexAttribPointer(index, length, type.type, false, stride, offset);
		GL32.glEnableVertexAttribArray(index);
	}

	public VertexAttribute(int length, Type type) {
		this.length = length;
		this.type = type;
	}

	/**
	 * Apply this attribute to currently bound VAO
	 */
	public void apply(int index, int stride, int offset) {
		glVertexAttribute(index, this.length, this.type, stride, offset);
	}

	public enum Type {
		BYTE(GL32.GL_BYTE, 1),
		UNSIGNED_BYTE(GL32.GL_UNSIGNED_BYTE, 1),
		SHORT(GL32.GL_SHORT, 2),
		UNSIGNED_SHORT(GL32.GL_UNSIGNED_SHORT, 2),
		INT(GL32.GL_INT, 4),
		UNSIGNED_INT(GL32.GL_UNSIGNED_INT, 4),
		FLOAT(GL32.GL_FLOAT, 4);

		public final int type;
		public final int size;

		Type(int type, int bytes) {
			this.type = type;
			this.size = bytes;
		}
	}

}
