package net.darktree.lt2d.graphics.image;

import java.util.ArrayList;
import java.util.List;

public class Atlas {

	public Texture texture;
	Image image;
	List<SpriteReference> taken;
	boolean frozen = false;

	private Atlas(Image image) {
		this.image = image;
		this.taken = new ArrayList<>();
	}

	public static Atlas createEmpty() {
		return new Atlas(new Image(16, 16, Image.Format.RGBA));
	}

	public static Atlas createBaked(Image image) {
		Atlas atlas = new Atlas(image);
		atlas.freeze();
		return atlas;
	}

	public Sprite at(int x, int y, int w, int h) {
		if (!this.frozen) {
			throw new RuntimeException("This atlas is not frozen!");
		}

		return new SpriteReference(x, y, x + w - 1, y + h - 1).sprite();
	}

	public SpriteReference add(Image sprite) {
		if (this.frozen) {
			throw new RuntimeException("This atlas is frozen!");
		}

		for (int x = 0; x < this.image.width(); x ++) {
			for (int y = 0; y < this.image.height(); y ++) {
				boolean fits = true;

				for (SpriteReference box : this.taken) {
					if (box.intersects(x, y, sprite)) {
						fits = false;
						break;
					}
				}

				if (fits && contains(x, y, sprite)) {
					SpriteReference reference = of(x, y, sprite);

					this.image.write(sprite, x, y);
					this.taken.add(reference);
					return reference;
				}
			}
		}

		Image atlas = new Image(this.image.width() + sprite.width(), this.image.height() + sprite.height(), this.image.format);
		atlas.write(this.image, 0, 0);
		this.image.close();
		this.image = atlas;

		return add(sprite);
	}

	public SpriteReference add(String resource) {
		try (Image texture = Image.of(resource, image.format)) {
			return add(texture);
		}
	}

	public void freeze() {
		this.frozen = true;
		this.texture = this.image.asTexture(false);
	}

	private boolean contains(int x, int y, Image image) {
		return this.image.checkBounds(x + image.width() - 1, y + image.height() - 1);
	}

	private SpriteReference of(int x, int y, Image image) {
		return new SpriteReference(x, y, x + image.width() - 1, y + image.height() - 1);
	}

	public class SpriteReference {

		public final int minX, minY, maxX, maxY;

		public boolean intersects(int x, int y, Image image) {
			int maxX = x + image.width() - 1;
			int maxY = y + image.height() - 1;
			return (this.minX <= maxX && this.maxX >= x) && (this.minY <= maxY && this.maxY >= y);
		}

		private SpriteReference(int minX, int minY, int maxX, int maxY) {
			this.minX = minX;
			this.minY = minY;
			this.maxX = maxX;
			this.maxY = maxY;
		}

		public Sprite sprite() {
			if (frozen) {
				return new Sprite(
						(this.minX + 0.001f) / (float) image.width(),
						(this.maxY + 0.999f) / (float) image.height(),
						(this.maxX + 0.999f) / (float) image.width(),
						(this.minY + 0.001f) / (float) image.height()
				);
			}else{
				throw new RuntimeException("Unable to query sprite of unfrozen atlas!");
			}
		}

	}

}
