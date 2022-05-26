package net.darktree.core.client.render;

import net.darktree.Main;
import net.darktree.core.client.Buffers;
import net.darktree.core.client.Shaders;
import net.darktree.core.client.Sprites;
import net.darktree.core.client.render.image.Font;
import net.darktree.core.client.render.image.Sprite;
import net.darktree.core.client.render.image.TextureConvertible;
import net.darktree.core.client.render.pipeline.Pipeline;
import net.darktree.core.client.render.pipeline.TexturedPipeline;
import net.darktree.core.client.render.vertex.Renderer;
import net.darktree.core.client.window.Input;
import net.darktree.core.client.window.Window;
import net.darktree.core.client.window.input.MouseButton;

import java.util.IdentityHashMap;
import java.util.Map;

public class ScreenRenderer {

	private static final Input INPUT = Window.INSTANCE.input();
	private static final Map<TextureConvertible, Pipeline> pipelines = new IdentityHashMap<>();

	private static final Pipeline quads = new TexturedPipeline(Buffers.TEXTURED.build(), Shaders.GUI, Sprites.ATLAS, pipeline -> {}, true);

	private static float psx, psy;
	private static float x, y;
	private static int ox, oy;
	private static float cr, cg, cb, ca;
	private static Sprite quadSprite;
	private static Font currentFont;
	private static Alignment currentAlignment = Alignment.LEFT;

	private static float projectMapIntoScreenX(int x) {
		return (x + Main.world.offsetX) * Main.world.scaleX;
	}

	private static float projectMapIntoScreenY(int y) {
		return (y + Main.world.offsetY) * Main.world.scaleY;
	}

	public static void registerFontPipeline(TextureConvertible texture) {
		pipelines.put(texture, new TexturedPipeline(Buffers.TEXTURED.build(), Shaders.TEXT, texture, pipeline -> {}, true));
	}

	/**
	 * Flush all geometry for rendering, and update screen size
	 */
	public static void flush() {
		float scale = INPUT.guiScale;
		psx = scale / Window.INSTANCE.width();
		psy = scale / Window.INSTANCE.height();

		quads.flush();

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

	/**
	 * Center the renderer at the specified map tile
	 */
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
	 * Set output color with no alpha, used for text coloring
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
	 * Sets the alignment for all subsequently rendered texts
	 */
	public static void setAlignment(Alignment alignment) {
		currentAlignment = alignment;
	}

	/**
	 * Render textured box
	 */
	public static boolean box(int right, int top) {
		Renderer.quad(quads.buffer, x + ox * psx, y + oy * psy, right * psx, top * psy, quadSprite, cr, cg, cb, ca);
		return isMouseOver(right, top);
	}

	/**
	 * Render textured box
	 */
	public static boolean box(int left, int right, int top, int bottom) {
		offset(-left, -bottom);
		return box(right + left, top + bottom);
	}

	/**
	 * Render text
	 */
	public static void text(String text, float size) {
		Renderer.text(text, currentFont, pipelines.get(currentFont).buffer, x + ox * psx, y + oy * psy, size * psx, size * psy, cr, cg, cb, ca, currentAlignment);
	}

	public static boolean isMouseOver(int right, int top) {
		float bx = x + ox * psx;
		float by = y + oy * psy;
		float mx = INPUT.getMouseScreenX();
		float my = INPUT.getMouseScreenY();

		if (mx < bx || my < by) {
			return false;
		}

		return !(mx > bx + right * psx || my > by + top * psy);
	}

	public static boolean button(String text, int count, int size, int height) {
		int width = height / 2;
		boolean hover = isMouseOver(width * (count + 2), height);

		int sx = ox;
		Alignment alignment = currentAlignment;

		if (hover) {
			setColor(0, 0, 0, 0.2f);

			if (INPUT.isButtonPressed(MouseButton.LEFT)) {
				setColor(0, 0, 0, 0.4f);
			}
		}

		setSprite(Sprites.BUTTON_LEFT);
		box(width, height);

		for (int i = 0; i < count; i ++) {
			setSprite(Sprites.BUTTON_CENTER);
			offset(width, 0);
			box(width, height);
		}

		setSprite(Sprites.BUTTON_RIGHT);
		offset(width, 0);
		box(width, height);

		ox = (int) (sx + ((2 + count) * width) / 2f) - 5;
		oy += (height - size) / 2f;

		setAlignment(Alignment.CENTER);
		text(text, size);

		setAlignment(alignment);
		setColor(0, 0, 0, 0);

		return hover && INPUT.hasClicked();
	}

}
