package net.darktree.opengl.shader;

import net.darktree.Logger;
import org.lwjgl.opengl.GL32;

public class Program implements AutoCloseable {

	private int id;

	private Program(int id) {
		this.id = id;
	}

	public void bind() {
		GL32.glUseProgram(this.id);
	}

	public int location(String name) {
		return GL32.glGetUniformLocation(this.id, name);
	}

	@Override
	public void close() {
		if (this.id != 0) {
			GL32.glDeleteProgram(this.id);
			this.id = 0;
		}
	}

	public static Builder create() {
		return new Builder();
	}

	// FIXME: cleanup the shaders
	public static class Builder {

		private final int id;

		private Builder() {
			this.id = GL32.glCreateProgram();
		}

		public Builder add(Shader shader) {
			GL32.glAttachShader(this.id, shader.id());
			return this;
		}

		public Builder add(String path, int type) {
			return add(Shader.of(path, type));
		}

		public Program link() {
			GL32.glLinkProgram(this.id);

			if (GL32.glGetProgrami(this.id, GL32.GL_LINK_STATUS) == GL32.GL_FALSE) {
				Logger.error(GL32.glGetProgramInfoLog(this.id));
				throw new RuntimeException("Failed to link program!");
			}

			return new Program(this.id);
		}

	}

}
