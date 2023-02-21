package net.darktree.warzone.util.math;

public class Vec2f {

	public final float x;
	public final float y;

	public Vec2f(float x, float y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Shorthand for {@code vector.dot(vector)}
	 */
	public float selfDot() {
		return x * x + y * y;
	}

	/**
	 * Get a vector that points from point (x1, y1) to point (x2, y2)
	 */
	public static Vec2f of(float x1, float y1, float x2, float y2) {
		return new Vec2f(x2 - x1, y2 - y1);
	}

	/**
	 * Get a vector that points from point a to point b
	 */
	public static Vec2f of(Vec2f a, Vec2f b) {
		return new Vec2f(b.x - a.x, b.y - a.y);
	}

	/**
	 * Calculate the dot product of two vectors
	 */
	public static float dot(Vec2f a, Vec2f b) {
		return a.x * b.x + a.y * b.y;
	}

}
