package net.darktree.warzone.client.render.image;

/**
 * Interface for classes that should be able to be treated as OpenGL Textures
 */
public interface TextureConvertible {

	/**
	 * Get a texture representing the same data
	 */
	Texture getTexture();

}
