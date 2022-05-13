package net.darktree.core.client.render;

import net.darktree.core.client.Buffers;
import net.darktree.core.client.Shaders;
import net.darktree.core.client.Uniforms;
import net.darktree.core.client.render.image.Font;
import net.darktree.core.client.render.image.Sprite;
import net.darktree.core.client.render.image.Texture;
import net.darktree.core.client.render.pipeline.Pipeline;
import net.darktree.core.client.render.pipeline.TexturedPipeline;
import net.darktree.core.client.render.vertex.Renderer;
import net.darktree.core.client.window.Input;
import net.darktree.core.client.window.Window;

import java.util.IdentityHashMap;
import java.util.Map;

public class ScreenRenderer {

	private static final Input INPUT = Window.INSTANCE.input();
	private static final Map<Object, Pipeline> pipelines = new IdentityHashMap<>();

	// register universal pipeline for quad rendering
	private static final TexturedPipeline quadPipeline = new TexturedPipeline(Buffers.TEXTURED.build(), Shaders.TEXTURED, (Texture) null, pipeline -> {
		Uniforms.SCALE.putFloats(1, 1).flush();
		Uniforms.OFFSET.putFloats(0, 0).flush();
	});

	private static float psx, psy;
	private static float x, y;
	private static int ox, oy;
	private static float cr, cg, cb, ca;
	private static Sprite quadSprite;
	private static Font currentFont;

	private static float projectMapIntoScreenX(int x) {
		return (x + INPUT.offsetX) * INPUT.scaleX;
	}

	private static float projectMapIntoScreenY(int y) {
		return (y + INPUT.offsetY) * INPUT.scaleY;
	}

	public static void registerFontPipeline(Font font) {
		pipelines.put(font, new TexturedPipeline(Buffers.TEXTURED.build(), Shaders.TEXT, font, pipeline -> {}));
	}

	/**
	 * Flush all geometry for rendering, and update screen size
	 */
	public static void flush() {
		float scale = INPUT.guiScale;
		psx = scale / Window.INSTANCE.width();
		psy = scale / Window.INSTANCE.height();

		quadPipeline.flush();

		for (Pipeline pipeline : pipelines.values()) {
			pipeline.flush();
		}
	}

	/**
	 * Center the renderer using screen space coordinates
	 */
	public static void centerAt(float sx, float sy) {
		x = sx;
		y = sy;
		ox = 0;
		oy = 0;
	}

	/**
	 * Center the renderer at the mouse
	 */
	public static void centerAtMouse() {
		centerAt(INPUT.getMouseScreenX(), INPUT.getMouseScreenY());
	}

	public static void centerAtTile(int x, int y) {
		centerAt(projectMapIntoScreenX(x), projectMapIntoScreenY(y));
	}

	/**
	 * Set the pixel offset for the renderer using GUI pixel coordinates
	 */
	public static void setOffset(int px, int py) {
		ox = px;
		oy = py;
	}

	/**
	 * Offset the renderer using GUI pixel coordinates
	 */
	public static void offset(int px, int py) {
		ox += px;
		oy += py;
	}

	/**
	 * Set output color, used for tinting textures and text coloring
	 */
	public static void setColor(float r, float g, float b, float a) {
		cr = r;
		cg = g;
		cb = b;
		ca = a;
	}

	/**
	 * Set output color with full alpha, used for text coloring
	 * for tinting quads use {@link #setColor(float r, float g, float b, float a)} and set alpha to >0
	 */
	public static void setColor(float r, float g, float b) {
		setColor(r, g, b, 0);
	}

	/**
	 * Set font for text renderer
	 */
	public static void setFont(Font font) {
		currentFont = font;
	}

	/**
	 * Set sprite for quad renderer
	 */
	public static void setSprite(Sprite sprite) {
		quadSprite = sprite;
	}

	/**
	 * Set texture for quad renderer
	 *
	 * <p><b>
	 * Warning: This operation will also force all previously written quads to be rendered
	 */
	// FIXME: put all gui textures into an atlas so that this doesn't need to flush
	public static void setTexture(Texture texture) {
		quadPipeline.flush();
		quadPipeline.setTexture(texture);
	}

	/**
	 * Set texture-sprite pair for quad renderer
	 *
	 * <p><b>
	 * Warning: This operation will also force all previously written quads to be rendered
	 */
	public static void setTexture(Texture texture, Sprite sprite) {
		setTexture(texture);
		setSprite(sprite);
	}

	/**
	 * Render textured box
	 */
	public static void box(int right, int top) {
		Renderer.quad(quadPipeline.buffer, x + ox * psx, y + oy * psy, right * psx, top * psy, quadSprite, cr, cg, cb, ca);
	}

	/**
	 * Render textured box
	 */
	public static void box(int left, int right, int top, int bottom) {
		offset(-left, -bottom);
		box(right + left, top + bottom);
	}

	/**
	 * Render text
	 */
	public static void text(String text, float size) {
		Renderer.text(text, currentFont, pipelines.get(currentFont).buffer, x + ox * psx, y + oy * psy, size * psx, size * psy, cr, cg, cb, ca);
	}

}
