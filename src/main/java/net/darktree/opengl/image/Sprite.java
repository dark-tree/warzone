package net.darktree.opengl.image;

public class Sprite {

	public static final Sprite IDENTITY = new Sprite(0, 0, 1, 1);
	private final float u1, v1, u2, v2;

	public Sprite(float u1, float v1, float u2, float v2) {
		this.u1 = u1;
		this.v1 = v1;
		this.u2 = u2;
		this.v2 = v2;
	}

	public float u1() {
		return u1;
	}

	public float v1() {
		return v1;
	}

	public float u2() {
		return u2;
	}

	public float v2() {
		return v2;
	}

}
