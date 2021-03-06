package net.darktree.core.client.render.image;

import org.lwjgl.opengl.GL32;

public class Texture implements AutoCloseable, TextureConvertible {

	private final Image image;
	private final int id;

	protected Texture(Image image) {
		this.image = image;
		this.id = GL32.glGenTextures();

		this.bind();

		GL32.glTexParameteri(GL32.GL_TEXTURE_2D, GL32.GL_TEXTURE_WRAP_S, GL32.GL_REPEAT);
		GL32.glTexParameteri(GL32.GL_TEXTURE_2D, GL32.GL_TEXTURE_WRAP_T, GL32.GL_REPEAT);
		GL32.glTexParameteri(GL32.GL_TEXTURE_2D, GL32.GL_TEXTURE_MIN_FILTER, GL32.GL_NEAREST);
		GL32.glTexParameteri(GL32.GL_TEXTURE_2D, GL32.GL_TEXTURE_MAG_FILTER, GL32.GL_NEAREST);
	}

	public void bind() {
		GL32.glBindTexture(GL32.GL_TEXTURE_2D, this.id);
	}

	public void upload() {
		this.bind();
		GL32.glTexImage2D(GL32.GL_TEXTURE_2D, 0, image.format.glFormat, image.width, image.height, 0, image.format.glFormat, GL32.GL_UNSIGNED_BYTE, image.getPointer(0,0));
	}

	@Override
	public void close() {
		GL32.glDeleteTextures(this.id);
		this.image.close();
	}

	@Override
	public Texture getTexture() {
		return this;
	}

}
