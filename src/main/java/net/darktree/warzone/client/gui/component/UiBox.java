package net.darktree.warzone.client.gui.component;

import net.darktree.warzone.client.Sprites;
import net.darktree.warzone.client.gui.DrawContext;
import net.darktree.warzone.client.gui.GridContext;
import net.darktree.warzone.client.gui.Inset;
import net.darktree.warzone.client.render.color.Color;
import net.darktree.warzone.client.render.color.ImmutableColor;

public class UiBox extends UiComponent {

	private static final Color COLOR = ImmutableColor.of(1, 1, 0);
	private final float thickness;

	protected UiBox(GridContext context, int x, int y, int width, int height, Inset inset, float thickness) {
		super(context, x, y, width, height, inset);
		this.thickness = thickness;
	}

	public static Builder of(int w, int h) {
		return new Builder().box(w, h);
	}

	@Override
	public void draw(DrawContext context, GridContext grid) {
		context.drawLineBox(box, thickness, Sprites.LINE_OVERLAY, 256, 0);
		drawDebugOverlay(context, COLOR);
	}

	static public class Builder extends UiComponent.Builder<UiBox, Builder> {

		private float thickness = 8;

		public Builder width(float thickness) {
			this.thickness = thickness;
			return self();
		}

		public Builder tint(Color color) {
			return self();
		}

		@Override
		protected Builder self() {
			return this;
		}

		@Override
		public UiBox build(GridContext context, int x, int y) {
			return new UiBox(context, x, y, width, height, inset, thickness);
		}

	}

}
