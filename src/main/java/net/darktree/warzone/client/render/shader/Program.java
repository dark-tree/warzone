package net.darktree.warzone.client.render.shader;

import net.darktree.warzone.client.render.GLManager;
import net.darktree.warzone.json.ShaderJsonBlob;
import net.darktree.warzone.util.Logger;
import net.darktree.warzone.util.Resources;
import org.lwjgl.opengl.GL32;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Program implements AutoCloseable {

	private int id;

	private Program(int id) {
		this.id = id;
		this.bind();
	}

	public void bind() {
		GLManager.useShader(id);
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
		Logger.info("Loading shader program '", path, "'");

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
	public static class Builder {

		private final int id;
		private final List<Shader> shaders = new ArrayList<>();

		private Builder() {
			this.id = GL32.glCreateProgram();
		}

		public Builder add(Shader shader) {
			GL32.glAttachShader(this.id, shader.id());
			shaders.add(shader);
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

			shaders.forEach(Shader::close);

			return new Program(this.id);
		}

	}

}
