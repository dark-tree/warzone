package net.darktree.warzone.client.render;

import net.darktree.warzone.Main;
import net.darktree.warzone.client.*;
import net.darktree.warzone.client.gui.DrawContext;
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

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Stack;

public class ScreenRenderer {

	private static final Input INPUT = Window.INSTANCE.input();
	private static final Map<TextureConvertible, Pipeline> pipelines = new IdentityHashMap<>();
	private static final HashMap<Integer, ScreenComponent> components = new HashMap<>();
	private static final Stack<Vec2i> offsets = new Stack<>();

	private static ColorMode mode = ColorMode.TINT;
	private static Pipeline current = null;
	private static Pipeline tinted, mixed;
	private static int identifier;
	private static float scale, psx, psy;
	private static float x, y;
	private static int ox, oy;
	private static float cr, cg, cb, ca;
	private static boolean focus;
	private static Sprite quadSprite;
	private static Font currentFont;
	private static Alignment currentAlignment = Alignment.LEFT;
	private static DrawContext CONTEXT = new DrawContext();

	private static float projectMapIntoScreenX(WorldView view, int x) {
		return (x + view.offsetX) * view.scaleX;
	}

	private static float projectMapIntoScreenY(WorldView view, int y) {
		return (y + view.offsetY) * view.scaleY;
	}

	public static void initializeQuadPipeline() {
		tinted = new Pipeline(Buffers.IMMEDIATE.build(), Shaders.TINTED, Sprites.ATLAS);
		mixed = new Pipeline(Buffers.IMMEDIATE.build(), Shaders.MIXED, Sprites.ATLAS);
	}

	public static void registerFontPipeline(TextureConvertible texture) {
		pipelines.put(texture, new Pipeline(Buffers.IMMEDIATE.build(), Shaders.TEXT, texture));
	}

	public static void appendDebugInfo(StringBuilder builder) {
		builder.append("SC=").append(components.size());
	}

	private static void use(Pipeline pipeline) {
		if (current != null && current != pipeline) {
			current.flush();
		}

		current = pipeline;
	}

	public static void preparePipeline() {
		use(mode == ColorMode.TINT ? tinted : mixed);
	}

	public static void setColorMode(ColorMode mode) {
		ScreenRenderer.mode = mode;
	}

	public static DrawContext getDrawContext() {
		return CONTEXT;
	}

	/**
	 * Flush all geometry for rendering, and update screen size
	 */
	public static void flush() {
		scale = INPUT.guiScale;
		psx = scale / Window.INSTANCE.width();
		psy = scale / Window.INSTANCE.height();

		if (current != null) current.flush();

		for (Pipeline pipeline : pipelines.values()) {
			pipeline.flush();
		}
	}

	/**
	 * Prepare screen renderer for the next frame
	 */
	public static void prepare() {
		identifier = 0;
	}

	/**
	 * Get the size of the offset stack
	 */
	public static int getStackSize() {
		return offsets.size();
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
	 * Get (or create default) screen component with the given ID
	 */
	private static ScreenComponent getComponent(int identifier) {
		ScreenComponent component = components.get(identifier);
		if (component == null) {
			component = new ScreenComponent();
			components.put(identifier, component);
		}

		return component;
	}

	/**
	 * Get the last used screen component
	 */
	public static ScreenComponent getLastComponent() {
		return getComponent(identifier - 1);
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
	public static void setColor(Color color) {
		cr = color.r();
		cg = color.g();
		cb = color.b();
		ca = color.a();
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

	public static void quad(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
		float tx = x + ox * psx;
		float ty = y + oy * psy;

		preparePipeline();
		Renderer.quad(current.buffer, tx + x1 * psx, ty + y1 * psy, tx + x2 * psx, ty + y2 * psy, tx + x3 * psx, ty + y3 * psy, tx + x4 * psx, ty + y4 * psy, quadSprite, cr, cg, cb, ca);
	}

	/**
	 * Render textured box
	 */
	public static boolean box(int right, int top) {
		preparePipeline();
		Renderer.quad(current.buffer, x + ox * psx, y + oy * psy, right * psx, top * psy, quadSprite, cr, cg, cb, ca);
		return isMouseOver(right, top);
	}

	/**
	 * Render textured centered box
	 */
	public static boolean centeredBox(int width, int height) {
		ScreenRenderer.offset(-width/2, -height/2);
		return box(width, height);
	}

	/**
	 * Render textured box
	 */
	public static boolean box(int left, int right, int top, int bottom) {
		offset(-left, -bottom);
		return box(right + left, top + bottom);
	}

	public static void vertex(float px, float py, float u, float v, float r, float g, float b, float a) {
		Renderer.vertex(current.buffer, x + (ox + px) * psx, y + (oy + py) * psy, u, v, r, g, b, a);
	}

	/**
	 * Render a line starting at current offset and pointing along the given vector
	 */
	public static void line(float width, int vx, int vy) {
		float sx = x + ox * psx;
		float sy = y + oy * psy;

		preparePipeline();
		Renderer.line(current.buffer, sx, sy, sx + vx * psx, sy + vy * psy, width * scale, cr, cg, cb, ca);
	}

	/**
	 * Draw a string based on its translation key
	 */
	public static void translatedText(float size, String value, Object... values) {
		text(size, Main.game.lang.formatted(value, values));
	}

	/**
	 * Draw a text
	 */
	public static void text(float size, CharSequence text) {
		Renderer.text(text.toString(), currentFont, pipelines.get(currentFont).buffer, x + ox * psx, y + oy * psy, size * psx, size * psy, cr, cg, cb, ca, currentAlignment);
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

	private static boolean button(int width, int height, boolean active, Color override) {
		boolean hover = active && isMouseOver(width, height);
		ScreenComponent component = getComponent(identifier ++);

		if (hover) {
			if (!component.selected) {
				Sounds.SELECT.play().setVolume(0.5f);
			}

			setColor(Colors.BUTTON_HOVER);

			if (INPUT.isButtonPressed(MouseButton.LEFT)) {
				setColor(Colors.BUTTON_PRESSED);
			}
		}else{
			setColor(active ? Colors.BUTTON_DEFAULT : Colors.BUTTON_INACTIVE);
		}

		if (override != null) {
			setColor(override);
		}

		component.selected = hover;
		return hover && Main.window.input().hasClicked();
	}

	public static boolean button(CharSequence text, int count, int size, int height, boolean active) {
		return button(text, count, size, height, active, null);
	}

	public static boolean button(CharSequence text, int count, int size, int height, boolean active, Color override) {
		push();
		int width = height / 2;
		boolean status = button(width * (count + 2), height, active, override);

		int sx = ox;
		Alignment alignment = currentAlignment;

		setColorMode(ColorMode.MIXED);
		setSprite(Sprites.BUTTON_PART_LEFT);
		box(width, height);

		for (int i = 0; i < count; i ++) {
			setSprite(Sprites.BUTTON_PART_CENTER);
			offset(width, 0);
			box(width, height);
		}

		setSprite(Sprites.BUTTON_PART_RIGHT);
		offset(width, 0);
		box(width, height);

		ox = (int) (sx + ((2 + count) * width) / 2f) - 5;
		oy += (height - size) / 2f;

		setColorMode(ColorMode.TINT);
		setAlignment(Alignment.CENTER);
		setColor(Colors.TEXT);
		text(size, text);

		setAlignment(alignment);
		pop();

		return status;
	}

	public static boolean button(Sprite sprite, int width, int height, boolean active) {
		return button(sprite, width, height, active, null);
	}

	public static boolean button(Sprite sprite, int width, int height, boolean active, Color override) {
		boolean status = button(width, height, active, override);

		ScreenRenderer.setSprite(sprite);
		ScreenRenderer.box(width, height);
		return status;
	}

}
