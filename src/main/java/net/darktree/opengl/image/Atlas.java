package net.darktree.opengl.image;

import java.util.ArrayList;
import java.util.List;

public class Atlas {

	Image atlas;
	List<SpriteReference> taken;
	boolean frozen = false;

	private Atlas(Image.Format format) {
		this.atlas = Image.of(16, 16, format);
		this.taken = new ArrayList<>();
	}

	public static Atlas create() {
		return new Atlas(Image.Format.RGBA);
	}

	public SpriteReference add(Image sprite) {
		if (this.frozen) {
			throw new RuntimeException("This atlas is frozen!");
		}

		for (int x = 0; x < this.atlas.width(); x ++) {
			for (int y = 0; y < this.atlas.height(); y ++) {
				boolean fits = true;

				for (SpriteReference box : this.taken) {
					if (box.intersects(x, y, sprite)) {
						fits = false;
						break;
					}
				}

				if (fits && contains(x, y, sprite)) {
					SpriteReference reference = of(x, y, sprite);

					this.atlas.write(sprite, x, y);
					this.taken.add(reference);
					return reference;
				}
			}
		}

		Image atlas = Image.of(this.atlas.width() + sprite.width(), this.atlas.height() + sprite.height(), this.atlas.format);
		atlas.write(this.atlas, 0, 0);
		this.atlas.close();
		this.atlas = atlas;

		return add(sprite);
	}

	public SpriteReference add(String resource) {
		try (Image texture = Image.of(resource, atlas.format)) {
			return add(texture);
		}
	}

	public void freeze() {
		this.frozen = true;
	}

	private boolean contains(int x, int y, Image image) {
		return this.atlas.checkBounds(x + image.width() - 1, y + image.height() - 1);
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
						this.minX / (float) atlas.width(),
						this.minY / (float) atlas.height(),
						this.maxX / (float) atlas.width(),
						this.maxY / (float) atlas.height()
				);
			}else{
				throw new RuntimeException("Unable to query sprite of unfrozen atlas!");
			}
		}

	}

}
