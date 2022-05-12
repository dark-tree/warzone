package net.darktree.core.client.render.image;

import java.util.*;
import java.util.stream.Collectors;

public class Atlas implements AutoCloseable {

	private Texture texture;
	private Image image;

	private final List<SpriteReference> taken = new ArrayList<>();
	private final Map<String, SpriteReference> sprites = new HashMap<>();
	boolean frozen = false;

	protected Atlas(Image image) {
		this.image = image;
	}

	public Atlas() {
		this(new Image(16, 16, Image.Format.RGBA));
	}

	public static Atlas bakedOf(Image image) {
		Atlas atlas = new Atlas(image);
		atlas.freeze();
		return atlas;
	}

	public Sprite at(int x, int y, int w, int h) {
		assertFrozen(true);

		return new SpriteReference(x, y, x + w - 1, y + h - 1).sprite();
	}

	public SpriteReference add(String identifier, Image sprite) {
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
					return ref;
				}
			}
		}

		Image atlas = new Image(this.image.width + sprite.width, this.image.height + sprite.height, this.image.format);
		atlas.write(this.image, 0, 0);
		this.image.close();
		this.image = atlas;

		return add(identifier, sprite);
	}

	public SpriteReference add(String resource, String identifier) {
		try (Image texture = Image.of(resource, image.format)) {
			return add(identifier, texture);
		}
	}

	public SpriteReference add(String resource) {
		return add(resource, resource);
	}

	public List<Map.Entry<String, Sprite>> freeze() {
		this.frozen = true;
		this.texture = this.image.asTexture();
		this.texture.upload();
		return this.sprites.entrySet().stream().map(entry -> new AbstractMap.SimpleEntry<>(
				entry.getKey(), entry.getValue().sprite())
		).collect(Collectors.toList());
	}

	public void assertFrozen(boolean value) {
		if (this.frozen != value) {
			throw new RuntimeException(value ? "Atlas is not frozen!" : "Atlas is frozen!");
		}
	}

	public Texture getTexture() {
		return texture;
	}

	public Image getImage() {
		return image;
	}

	@Override
	public void close() throws Exception {
		if (texture != null) {
			texture.close();
		}else{
			image.close();
		}
	}

	public class SpriteReference {

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
					(this.minX + 0.001f) / (float) image.width,
					(this.maxY + 0.999f) / (float) image.height,
					(this.maxX + 0.999f) / (float) image.width,
					(this.minY + 0.001f) / (float) image.height
			);
		}

	}

}
