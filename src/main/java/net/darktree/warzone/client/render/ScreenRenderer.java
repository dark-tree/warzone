package net.darktree.warzone.client.render;

import net.darktree.warzone.Main;
import net.darktree.warzone.client.*;
import net.darktree.warzone.client.render.color.Color;
import net.darktree.warzone.client.render.image.Font;
import net.darktree.warzone.client.render.image.Sprite;
import net.darktree.warzone.client.render.image.TextureConvertible;
import net.darktree.warzone.client.render.pipeline.Pipeline;
import net.darktree.warzone.client.render.vertex.Renderer;
import net.darktree.warzone.client.window.Input;
import net.darktree.warzone.client.window.Window;
import net.darktree.warzone.client.window.input.MouseButton;
import net.darktree.warzone.util.math.Vec2i;
import net.darktree.warzone.world.WorldView;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Stack;

public class ScreenRenderer {

	private static final Input INPUT = Window.INSTANCE.input();
	private static final Map<TextureConvertible, Pipeline> pipelines = new IdentityHashMap<>();

	private static final Pipeline quads = new Pipeline(Buffers.IMMEDIATE.build(), Shaders.GUI, Sprites.ATLAS);

	private static float scale, psx, psy;
	private static float x, y;
	private static int ox, oy;
	private static float cr, cg, cb, ca;
	private static boolean focus;
	private static Sprite quadSprite;
	private static Font currentFont;
	private static Alignment currentAlignment = Alignment.LEFT;
	private static final Stack<Vec2i> offsets = new Stack<>();

	private static float projectMapIntoScreenX(WorldView view, int x) {
		return (x + view.offsetX) * view.scaleX;
	}

	private static float projectMapIntoScreenY(WorldView view, int y) {
		return (y + view.offsetY) * view.scaleY;
	}

	public static void registerFontPipeline(TextureConvertible texture) {
		pipelines.put(texture, new Pipeline(Buffers.IMMEDIATE.build(), Shaders.TEXT, texture));
	}

	/**
	 * Flush all geometry for rendering, and update screen size
	 */
	public static void flush() {
		scale = INPUT.guiScale;
		psx = scale / Window.INSTANCE.width();
		psy = scale / Window.INSTANCE.height();

		quads.flush();

		for (Pipeline pipeline : pipelines.values()) {
			pipeline.flush();
		}
	}

	/**
	 * Save the current offset onto a stack
	 */
	public static void push() {
		offsets.push(new Vec2i(ox, oy));
	}

	/**
	 * Load the last offset from a stack
	 */
	public static void pop() {
		Vec2i offset = offsets.pop();

		ox = offset.x;
		oy = offset.y;
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
	public static void centerAtTile(WorldView view, int x, int y) {
		centerAt(projectMapIntoScreenX(view, x), projectMapIntoScreenY(view, y));
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
	@Deprecated
	public static void setColor(float r, float g, float b, float a) {
		cr = r;
		cg = g;
		cb = b;
		ca = a;
	}

	/**
	 * Set output color, used for tinting textures and text coloring
	 */
	public static void setColor(Color color) {
		cr = color.r;
		cg = color.g;
		cb = color.b;
		ca = color.a;
	}

	/**
	 * Set output color with no alpha, used for text coloring
	 * for tinting quads use {@link #setColor(float r, float g, float b, float a)} and set alpha to >0
	 */
	public static void setColor(float r, float g, float b) {
		setColor(r, g, b, 0);
	}

	/**
	 * Set focus context for dynamic elements like buttons
	 */
	public static void setFocus(boolean flag) {
		focus = flag;
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
	 * Render a line starting at current offset and pointing along the given vector
	 */
	public static void line(float width, int vx, int vy) {
		float sx = x + ox * psx;
		float sy = y + oy * psy;

		Renderer.line(quads.buffer, sx, sy, sx + vx * psx, sy + vy * psy, width * scale, cr, cg, cb, ca);
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

		return focus && !(mx > bx + right * psx || my > by + top * psy);
	}

	private static boolean button(int width, int height, boolean active) {
		boolean hover = active && isMouseOver(width, height);

		if (hover) {
			setColor(Colors.BUTTON_HOVER);

			if (INPUT.isButtonPressed(MouseButton.LEFT)) {
				setColor(Colors.BUTTON_PRESSED);
			}
		}else{
			setColor(active ? Colors.BUTTON_DEFAULT : Colors.BUTTON_INACTIVE);
		}

		boolean clicked = hover && Main.window.input().hasClicked();

		if (clicked) {
			Sounds.PEN_CLICK.play();
		}

		return clicked;
	}

	public static boolean button(String text, int count, int size, int height, boolean active) {
		int width = height / 2;
		boolean status = button(width * (count + 2), height, active);

		int sx = ox;
		Alignment alignment = currentAlignment;

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

		return status;
	}

	public static boolean button(Sprite sprite, int width, int height, boolean active) {
		boolean status = button(width, height, active);

		ScreenRenderer.setSprite(sprite);
		ScreenRenderer.box(width, height);
		return status;
	}

}
