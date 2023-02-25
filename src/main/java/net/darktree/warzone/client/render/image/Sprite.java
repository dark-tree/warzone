package net.darktree.warzone.client.render.image;

public interface Sprite extends SpriteConvertible {

	Sprite IDENTITY = new NativeSprite(0, 1, 1, 0);
	Sprite NULL = new NativeSprite(0, 0, 0, 0);

	float u1();
	float v1();
	float u2();
	float v2();

	@Override
	default Sprite sprite() {
		return this;
	}

}
