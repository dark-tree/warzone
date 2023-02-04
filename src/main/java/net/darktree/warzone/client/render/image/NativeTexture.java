package net.darktree.warzone.client.render.image;

import net.darktree.warzone.client.render.GLManager;
import org.lwjgl.opengl.GL32;

public class NativeTexture extends Texture {

	private final Image image;
	private final int id;

	protected NativeTexture(Image image) {
		this.image = image;
		this.id = GL32.glGenTextures();

		this.bind();

		GL32.glTexParameteri(GL32.GL_TEXTURE_2D, GL32.GL_TEXTURE_WRAP_S, GL32.GL_REPEAT);
		GL32.glTexParameteri(GL32.GL_TEXTURE_2D, GL32.GL_TEXTURE_WRAP_T, GL32.GL_REPEAT);
		GL32.glTexParameteri(GL32.GL_TEXTURE_2D, GL32.GL_TEXTURE_MIN_FILTER, GL32.GL_LINEAR);
		GL32.glTexParameteri(GL32.GL_TEXTURE_2D, GL32.GL_TEXTURE_MAG_FILTER, GL32.GL_LINEAR);
	}

	@Override
	public void bind() {
		GLManager.useTexture(id);
	}

	@Override
	public void upload() {
		this.bind();
		GL32.glTexImage2D(GL32.GL_TEXTURE_2D, 0, image.format.glFormat, image.width, image.height, 0, image.format.glFormat, GL32.GL_UNSIGNED_BYTE, image.getPointer(0,0));
	}

	@Override
	public void close() {
		GL32.glDeleteTextures(this.id);
		this.image.close();
	}

}
