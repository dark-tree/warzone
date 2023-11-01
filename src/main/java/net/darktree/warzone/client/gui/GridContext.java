package net.darktree.warzone.client.gui;

import net.darktree.warzone.client.Colors;
import net.darktree.warzone.client.Sprites;
import net.darktree.warzone.client.gui.component.UiComponent;
import net.darktree.warzone.client.gui.state.GridState;
import net.darktree.warzone.client.render.ColorMode;
import net.darktree.warzone.client.render.ScreenRenderer;
import net.darktree.warzone.client.render.color.Color;
import net.darktree.warzone.client.render.color.ImmutableColor;
import net.darktree.warzone.client.window.Window;
import net.darktree.warzone.client.window.input.Event;
import net.darktree.warzone.util.BoundingBox;

public class GridContext {

	private static final Color COLOR = ImmutableColor.of(0, 1, 1);
	public static final int SIZE = 32;
	public static final int GAP = 2;

	public final int width;
	public final int height;
	public final int size;
	private final int ox;
	private final int oy;
	private final float ax;
	private final float ay;
	private final BoundingBox box;
	private final GridState state = new GridState();

	private UiComponent component;

	public GridContext(int width, int height, int size) {
		this(width, height, size, -width * size / 2, -height * size / 2, 0, 0);
	}

	public GridContext(int width, int height, int size, int ox, int oy, float ax, float ay) {
		this.width = width;
		this.height = height;
		this.size = size;
		this.ox = ox;
		this.oy = oy;
		this.ax = ax;
		this.ay = ay;
		this.box = new BoundingBox(0, 0, width, height).scale(size);
	}

	public BoundingBox box(int x, int y, int w, int h, Inset inset) {
		float gap = (GAP / (float) SIZE);
		return new BoundingBox(x + inset.left + gap, y + inset.bottom + gap, x + w - inset.right, y + h - inset.top).scale(size);
	}

	public void setModel(ModelBuilder builder) {
		this.component = builder.build(this, 0, 0);
	}

	public GridState getState() {
		return state;
	}

	public float mouseX() {
		return Window.INSTANCE.input().getMouseUiX() - ox;
	}

	public float mouseY() {
		return Window.INSTANCE.input().getMouseUiY() - oy;
	}

	public boolean isMouseIn(BoundingBox box) {
		return box.contains(mouseX(), mouseY());
	}

	public void react(Event event) {
		if (component != null && UiComponent.shouldAccept(box, event, this)) {
			component.react(event, this);
		}
	}

	public void draw(DrawContext context) {
		if (component != null) {
			ScreenRenderer.setColorMode(ColorMode.TINT);
			ScreenRenderer.preparePipeline();

			ScreenRenderer.push();
			ScreenRenderer.centerAt(ax, ay);
			ScreenRenderer.setOffset(ox, oy);

			// render background in mixed mode
			ScreenRenderer.setColorMode(ColorMode.MIXED);
			ScreenRenderer.preparePipeline();
			context.drawNinePatch(0, 0, width, height, size, true, true, Sprites.GRID_NINE_PATCH, Colors.UI_GRID);

			// switch back to tinted mode and draw components
			ScreenRenderer.setColorMode(ColorMode.TINT);
			ScreenRenderer.preparePipeline();
			component.draw(context, this);

			if (context.isDebugMode()) {
				context.drawDebugBox(box, 2f, COLOR);
			}

			ScreenRenderer.pop();
		}
	}

}
