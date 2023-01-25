package net.darktree.warzone.client.render.image;

import net.darktree.warzone.util.Logger;
import net.darktree.warzone.util.Resources;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Atlas implements AutoCloseable, TextureConvertible {

	private Texture texture;
	private Image image;

	private final List<SpriteReference> taken = new ArrayList<>();
	private final Map<Object, SpriteConvertible> sprites = new HashMap<>();
	private boolean frozen = false;

	protected Atlas(Image image) {
		this.image = image;
	}

	protected Atlas() {
		this(new Image(16, 16, Image.Format.RGBA));
	}

	/**
	 * Used for creating an atlas from multiple smaller images
	 */
	public static Atlas stitchedOf(String path) {
		Atlas atlas = new Atlas();

		Path root = Resources.location(path);

		if (root == null) {
			Logger.error("Unable to locate atlas directory '", path, "'!");
			throw new RuntimeException("Unable to load atlas!");
		}

		long start = System.currentTimeMillis();

		atlas.loadAll(path, path, root);
		atlas.freeze();

		Logger.info("Texture atlas '", path, "' stitched! (took ", System.currentTimeMillis() - start, "ms)");

		return atlas;
	}

	/**
	 * Used for creating an atlas from a single (atlas) image
	 */
	public static Atlas identityOf(Image image) {
		Atlas atlas = new Atlas(image);
		atlas.freeze();

		return atlas;
	}

	/**
	 * Get sprite from atlas by its coordinates, the result of this method call should be cached and reused
	 */
	public Sprite at(int x, int y, int w, int h) {
		return new SpriteReference(x, y, x + w - 1, y + h - 1).sprite();
	}

	/**
	 * Get sprite from atlas by its identifier (in stitched atlases this is the relative file path)
	 */
	public Sprite getSprite(Object identifier) {
		return Objects.requireNonNull(this.sprites.get(identifier)).sprite();
	}

	protected void addImage(Object identifier, Image sprite) {
		assertFrozen(false);

		for (int x = 0; x < this.image.width; x ++) {
			for (int y = 0; y < this.image.height; y ++) {
				boolean fits = true;

				for (SpriteReference box : this.taken) {
					if (box.intersects(x, y, sprite)) {
						fits = false;
						break;
					}
				}

				if (fits && image.checkBounds(x + sprite.width - 1, y + sprite.height - 1)) {
					SpriteReference ref = new SpriteReference(x, y, x + sprite.width - 1, y + sprite.height - 1);

					this.image.write(sprite, x, y);
					this.taken.add(ref);
					this.sprites.put(identifier, ref);
					return;
				}
			}
		}

		Image atlas = new Image(this.image.width + sprite.width, this.image.height + sprite.height, this.image.format);
		atlas.write(this.image, 0, 0);
		this.image.close();
		this.image = atlas;

		addImage(identifier, sprite);
	}

	protected void addPath(String resource, Object identifier) {
		try (Image texture = Image.of(resource, image.format)) {
			addImage(identifier, texture);
		}
	}

	private void loadAll(String source, String resource, Path root) {
		Resources.listing(resource).forEach(path -> {
			if (Files.isDirectory(path)) {
				loadAll(source, resource + "/" + path.getFileName(), root);
			}

			String identifier = root.relativize(path).toString().replace('\\', '/');

			if (identifier.endsWith(".png")) {
				addPath(source + "/" + identifier, identifier);
			}
		});
	}

	protected void freeze() {
		this.frozen = true;
		this.texture = this.image.asTexture();
		this.texture.upload();
		this.taken.clear();

		this.sprites.keySet().forEach(key -> {
			SpriteConvertible convertible = this.sprites.get(key);
			this.sprites.put(key, convertible.sprite());
		});
	}

	protected void assertFrozen(boolean value) {
		if (this.frozen != value) {
			throw new RuntimeException(value ? "Atlas is not frozen!" : "Atlas is frozen!");
		}
	}

	@Override
	public Texture getTexture() {
		return texture;
	}

	@Override
	public void close() throws Exception {
		if (texture != null) {
			texture.close();
		}else{
			image.close();
		}
	}

	protected class SpriteReference implements SpriteConvertible {

		public final int minX, minY, maxX, maxY;

		public boolean intersects(int x, int y, Image image) {
			int maxX = x + image.width - 1;
			int maxY = y + image.height - 1;
			return (this.minX <= maxX && this.maxX >= x) && (this.minY <= maxY && this.maxY >= y);
		}

		private SpriteReference(int minX, int minY, int maxX, int maxY) {
			this.minX = minX;
			this.minY = minY;
			this.maxX = maxX;
			this.maxY = maxY;
		}

		public Sprite sprite() {
			assertFrozen(true);

			return new Sprite(
					(this.minX + 0.5f) / (float) image.width,
					(this.maxY + 0.5f) / (float) image.height,
					(this.maxX + 0.5f) / (float) image.width,
					(this.minY + 0.5f) / (float) image.height
			);
		}

	}

}
