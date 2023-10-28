package net.darktree.warzone.util;

public class BoundingBox {

	public final float x1, y1, x2, y2;

	public BoundingBox(float x1, float y1, float x2, float y2) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}

	public float width() {
		return x2 - x1;
	}

	public float height() {
		return y2 - y1;
	}

	public BoundingBox scale(float scalar) {
		return new BoundingBox(x1 * scalar, y1 * scalar, x2 * scalar, y2 * scalar);
	}

	public BoundingBox inset(float scalar) {
		return new BoundingBox(x1 + scalar, y1 + scalar, x2 - scalar, y2 - scalar);
	}

	public boolean contains(float x, float y) {
		return x > x1 && x < x2 && y > y1 && y < y2;
	}
}
