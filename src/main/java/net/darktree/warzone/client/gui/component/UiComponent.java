package net.darktree.warzone.client.gui.component;

import net.darktree.warzone.client.gui.ComponentBuilder;
import net.darktree.warzone.client.gui.DrawContext;
import net.darktree.warzone.client.gui.GridContext;
import net.darktree.warzone.client.gui.Inset;
import net.darktree.warzone.client.gui.state.Property;
import net.darktree.warzone.client.gui.state.UiIdentifier;
import net.darktree.warzone.client.render.color.Color;
import net.darktree.warzone.client.window.input.Event;
import net.darktree.warzone.client.window.input.EventType;
import net.darktree.warzone.util.BoundingBox;

public abstract class UiComponent {

	protected final int x;
	protected final int y;
	protected final int width;
	protected final int height;
	protected final Inset inset;
	protected final BoundingBox box;
	protected final UiIdentifier identifier;

	protected UiComponent(GridContext context, int x, int y, int width, int height, Inset inset) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.inset = inset;
		this.box = context.box(x, y, width, height, inset);
		this.identifier = new UiIdentifier(this, x, y);
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public BoundingBox getBox() {
		return box;
	}

	public UiIdentifier getIdentifier() {
		return identifier;
	}

	/**
	 * Called when a UI State property associated with this component changes
	 */
	public void onUpdate(Property property, boolean enabled) {

	}

	/**
	 * Called when an input event relevant to this grid context and screen is triggered by the user
	 */
	public void onEvent(Event event, GridContext grid) {
		// no-op
	}

	protected void drawDebugOverlay(DrawContext context, Color color) {
		if (context.isDebugMode()) {
			context.drawDebugBox(box.inset(1f), 2f, color);
		}
	}

	public static boolean shouldAccept(BoundingBox box, Event event, GridContext grid) {
		if (event.getType() == EventType.KEYBOARD) {
			return true;
		}

		if (event.getType() == EventType.MOUSE) {
			return grid.isMouseIn(box);
		}

		return false;
	}

	public abstract void draw(DrawContext context, GridContext grid);

	static public abstract class Builder<C extends UiComponent, B extends Builder<C, B>> implements ComponentBuilder<C> {

		protected int width = 1;
		protected int height = 1;
		protected Inset inset = Inset.empty();

		public B box(int w, int h) {
			this.width = w;
			this.height = h;
			return self();
		}

		public B inset(float margin) {
			return inset(margin, margin);
		}

		public B inset(float vertical, float horizontal) {
			return inset(vertical, vertical, horizontal, horizontal);
		}

		public B inset(float top, float bottom, float right, float left) {
			this.inset = new Inset(top, bottom, right, left);
			return self();
		}

		protected abstract B self();

		@Override
		public int getWidth() {
			return width;
		}

		@Override
		public int getHeight() {
			return height;
		}

		@Override
		public abstract C build(GridContext context, int x, int y);

	}

}
