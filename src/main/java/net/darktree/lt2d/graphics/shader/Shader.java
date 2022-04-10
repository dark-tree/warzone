package net.darktree.lt2d.graphics.shader;

import net.darktree.lt2d.util.Logger;
import net.darktree.lt2d.util.Resources;
import org.lwjgl.opengl.GL32;

import java.io.IOException;

public class Shader implements AutoCloseable {

	private int id;

	public Shader(String source, int type) {
		this.id = GL32.glCreateShader(type);
		GL32.glShaderSource(this.id, source);
		GL32.glCompileShader(this.id);

		if (GL32.glGetShaderi(this.id, GL32.GL_COMPILE_STATUS) == GL32.GL_FALSE) {
			Logger.error(GL32.glGetShaderInfoLog(this.id));
			throw new RuntimeException("Failed to compile shader!");
		}
	}

	@Override
	public void close() {
		if (this.id != 0) {
			GL32.glDeleteShader(this.id);
			this.id = 0;
		}
	}

	public int id() {
		return this.id;
	}

	public static Shader of(String path, int type) {
		Logger.info("Loading shader '", path, "'");

		try {
			return new Shader(Resources.contents(path), type);
		} catch (IOException e) {
			throw new RuntimeException("Failed to read shader file!", e);
		}
	}

}
