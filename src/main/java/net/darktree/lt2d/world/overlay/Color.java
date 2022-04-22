package net.darktree.lt2d.world.overlay;

public class Color {
	public float r, g, b, a;

	public void set(float r, float g, float b, float a) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}

	public void set(float r, float g, float b) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = 0.2f;
	}

	public void clear() {
		this.a = 0;
	}
}
