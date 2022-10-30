package net.darktree.warzone.client.render.image;

public abstract class Texture implements AutoCloseable, TextureConvertible {

	abstract public void bind();
	abstract public void upload();

	@Override
	public final Texture getTexture() {
		return this;
	}

}
