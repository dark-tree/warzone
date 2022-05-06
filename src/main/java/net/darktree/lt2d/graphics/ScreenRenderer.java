package net.darktree.lt2d.graphics;

import net.darktree.game.rendering.Buffers;
import net.darktree.game.rendering.Shaders;
import net.darktree.game.rendering.Uniforms;
import net.darktree.lt2d.graphics.image.Sprite;
import net.darktree.lt2d.graphics.image.Texture;
import net.darktree.lt2d.graphics.pipeline.TexturedPipeline;
import net.darktree.lt2d.graphics.vertex.Renderer;
import org.jetbrains.annotations.ApiStatus;

public class ScreenRenderer {

	private static final ScreenRenderer INSTANCE = new ScreenRenderer();
	private static final Input INPUT = Window.INSTANCE.input();

	private final TexturedPipeline pipeline;
	private float x, y;
	private int ox, oy;
	private Sprite sprite;

	private boolean repeating = false;
	private float fx, fy;

	public ScreenRenderer() {
		this.pipeline = new TexturedPipeline(Buffers.TEXTURED.build(), Shaders.TEXTURED, (Texture) null, pipeline -> {
			Uniforms.SCALE.putFloats(1, 1).flush();
			Uniforms.OFFSET.putFloats(0, 0).flush();
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
		this.repeating = false;
		return this;
	}

	@ApiStatus.Experimental
	public ScreenRenderer repeating(float fx, float fy) {
		this.fx = fx;
		this.fy = fy;
		this.repeating = true;
		return this;
	}

	public ScreenRenderer box(int right, int top) {
		float sc = INPUT.guiScale;
		float px = sc / Window.INSTANCE.width();
		float py = sc / Window.INSTANCE.height();

		Sprite target = this.sprite;

		if (repeating) {
			target = new Sprite(this.sprite.u1(), this.sprite.v1(), this.sprite.u2() * this.fx, this.sprite.v2() * this.fy);
		}

		Renderer.quad(this.pipeline.buffer, this.x + this.ox * px, this.y + this.oy * py, right * px, top * py, target, 1, 1, 1, 0);
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
