package net.darktree.warzone.client.render.image;

import org.checkerframework.checker.nullness.qual.NonNull;

public class WrappedSprite implements Sprite {

	private Sprite sprite;

	public WrappedSprite(@NonNull Sprite sprite) {
		this.sprite = sprite;
	}

	public void setSprite(@NonNull Sprite sprite) {
		this.sprite = sprite;
	}

	@Override
	public float u1() {
		return sprite.u1();
	}

	@Override
	public float v1() {
		return sprite.v1();
	}

	@Override
	public float u2() {
		return sprite.u2();
	}

	@Override
	public float v2() {
		return sprite.v2();
	}

}
