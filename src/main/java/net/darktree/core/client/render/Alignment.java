package net.darktree.core.client.render;

public enum Alignment {
	LEFT(0.0f),
	CENTER(0.5f),
	RIGHT(1.0f);

	private final float multiplier;

	Alignment(float multiplier) {
		this.multiplier = multiplier;
	}

	public float getOffset(float size) {
		return -multiplier * size;
	}
}
