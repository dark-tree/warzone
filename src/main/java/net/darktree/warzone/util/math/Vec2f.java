package net.darktree.warzone.util.math;

public class Vec2f {
	public final float x;
	public final float y;

	public Vec2f(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public float dot() {
		return x * x + y * y;
	}

	public static Vec2f of(float x1, float y1, float x2, float y2) {
		return new Vec2f(x2 - x1, y2 - y1);
	}

	public static Vec2f of(Vec2f a, Vec2f b) {
		return new Vec2f(b.x - a.x, b.y - a.y);
	}

	public static float dot(Vec2f a, Vec2f b) {
		return a.x * b.x + a.y * b.y;
	}
}
