package net.darktree.opengl;

import net.darktree.game.rendering.Buffers;
import net.darktree.game.rendering.Shaders;
import net.darktree.game.rendering.Uniforms;
import net.darktree.opengl.image.Sprite;
import net.darktree.opengl.image.Texture;
import net.darktree.opengl.pipeline.TexturedPipeline;
import net.darktree.opengl.vertex.Renderer;

public class ScreenRenderer {

	private static final ScreenRenderer INSTANCE = new ScreenRenderer();
	private static final Input INPUT = Window.INSTANCE.input();

	private final TexturedPipeline pipeline;
	private float x, y;
	private int ox, oy;
	private Sprite sprite;

	public ScreenRenderer() {
		this.pipeline = new TexturedPipeline(Buffers.TEXTURED.build(), Shaders.TEXTURED, (Texture) null, pipeline -> {
			Uniforms.SCALE.putFloat(1).putFloat(1).flush();
		});
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

	public ScreenRenderer sprite(Texture texture, Sprite sprite) {
		this.sprite = sprite;
		this.pipeline.texture = texture;
		return this;
	}

	public ScreenRenderer box(int right, int top) {
		float sc = INPUT.guiScale;
		float px = sc / Window.INSTANCE.width();
		float py = sc / Window.INSTANCE.height();

		Renderer.quad(this.pipeline.buffer, this.x + this.ox * px, this.y + this.oy * py, right * px, top * py, this.sprite);
		return this;
	}

	public ScreenRenderer box(int left, int right, int top, int bottom) {
		return this.offset(-left, -bottom).box(right + left, top + bottom);
	}

	public ScreenRenderer next() {
		this.pipeline.flush();
		return this.reset();
	}

}
