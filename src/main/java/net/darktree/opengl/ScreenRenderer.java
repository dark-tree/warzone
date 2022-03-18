package net.darktree.opengl;

import net.darktree.opengl.image.Sprite;
import net.darktree.opengl.vertex.Renderer;
import net.darktree.opengl.vertex.VertexBuffer;

import static org.lwjgl.opengl.GL11.glDrawArrays;

public class ScreenRenderer {

	private static final ScreenRenderer INSTANCE = new ScreenRenderer();

	private final VertexBuffer buffer;
	private float x, y;
	private int ox, oy;
	private Sprite sprite;

	public ScreenRenderer() {
		VertexBuffer.Builder builder = VertexBuffer.create();
		builder.attribute(2); // xy
		builder.attribute(2); // uv

		this.buffer = builder.build();
	}

	public static ScreenRenderer from(float x, float y) {
		return INSTANCE.at(x, y);
	}

	public ScreenRenderer at(float x, float y) {
		this.x = x;
		this.y = y;
		return this;
	}

	public ScreenRenderer offset(int x, int y) {
		this.ox += x;
		this.oy += y;
		return this;
	}

	public ScreenRenderer reset() {
		this.ox = 0;
		this.oy = 0;
		return this;
	}

	public ScreenRenderer sprite(Sprite sprite) {
		this.sprite = sprite;
		return this;
	}

	public ScreenRenderer box(int right, int top) {
		float px = 1f / Window.INSTANCE.width();
		float py = 1f / Window.INSTANCE.height();

		Renderer.quad(this.buffer, this.x + this.ox * px, this.y + this.oy * py, right * px, top * py, this.sprite);
		return this;
	}

	public ScreenRenderer box(int left, int right, int top, int bottom) {
		return this.offset(-left, -bottom).box(right + left, top + bottom);
	}

	public ScreenRenderer next() {
		this.buffer.bind();
		glDrawArrays(this.buffer.primitive, 0, this.buffer.count());
		this.buffer.clear();

		return this.reset();
	}

}
