package net.darktree.core.client.render.shader;

import net.darktree.core.json.ShaderJsonBlob;
import net.darktree.core.util.Logger;
import net.darktree.core.util.Resources;
import org.lwjgl.opengl.GL32;

import java.io.IOException;

public class Program implements AutoCloseable {

	private int id;

	private Program(int id) {
		this.id = id;
		this.bind();
	}

	public void bind() {
		GL32.glUseProgram(this.id);
	}

	public int location(String name) {
		return GL32.glGetUniformLocation(this.id, name);
	}

	public Uniform uniform(String name, Uniform.Sender sender) {
		return new Uniform(location(name), sender);
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

	public static Program from(String path) {
		try {
			ShaderJsonBlob blob = Resources.json("shader/" + path + ".json", ShaderJsonBlob.class);

			return Program.create()
					.add("shader/" + blob.vertex, GL32.GL_VERTEX_SHADER)
					.add("shader/" + blob.fragment, GL32.GL_FRAGMENT_SHADER)
					.link();

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
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
