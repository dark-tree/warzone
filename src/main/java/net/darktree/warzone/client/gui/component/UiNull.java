package net.darktree.warzone.client.gui.component;

import net.darktree.warzone.client.gui.ComponentBuilder;
import net.darktree.warzone.client.gui.DrawContext;
import net.darktree.warzone.client.gui.GridContext;
import net.darktree.warzone.client.gui.Inset;
import net.darktree.warzone.client.render.color.Color;
import net.darktree.warzone.client.render.color.ImmutableColor;

public class UiNull extends UiComponent {

	private static final Color COLOR = ImmutableColor.of(0, 0, 0);

	private UiNull(GridContext context, int x, int y, int width, int height, Inset inset) {
		super(context, x, y, width, height, inset);
	}

	public static ComponentBuilder<?> of(int w, int h) {
		return new Builder().box(w, h);
	}

	@Override
	public void draw(DrawContext context, GridContext grid) {
		drawDebugOverlay(context, COLOR);
	}

	static public class Builder extends UiComponent.Builder<UiNull, Builder> {

		@Override
		protected Builder self() {
			return this;
		}

		@Override
		public UiNull build(GridContext context, int x, int y) {
			return new UiNull(context, x, y, width, height, inset);
		}

	}

}
