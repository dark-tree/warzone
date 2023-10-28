package net.darktree.warzone.client.render.image;

public class NinePatch {

	private final Sprite[] sprites;
	public final int width;
	public final int height;
	public final int edge;

	public NinePatch(Sprite[] sprites, int w, int h, int e) {
		this.sprites = sprites;
		this.width = w;
		this.height = h;
		this.edge = e;
	}

	public Sprite getSegment(int x, int y) {
		return sprites[x + y * 3];
	}

}
