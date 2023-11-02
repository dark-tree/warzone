package net.darktree.warzone.client.gui;

public class Inset {

	private static final Inset EMPTY = new Inset(0, 0, 0, 0);
	public final float top, bottom, right, left;

	public Inset(float top, float bottom, float right, float left) {
		this.top = top;
		this.bottom = bottom;
		this.right = right;
		this.left = left;
	}

	public static Inset empty() {
		return EMPTY;
	}

	public float width() {
		return Math.abs(left - right);
	}

	public float height() {
		return Math.abs(top - bottom);
	}

}
