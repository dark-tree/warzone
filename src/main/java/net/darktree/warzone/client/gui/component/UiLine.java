package net.darktree.warzone.client.gui.component;

import net.darktree.warzone.client.Colors;
import net.darktree.warzone.client.Sprites;
import net.darktree.warzone.client.gui.DrawContext;
import net.darktree.warzone.client.gui.GridContext;
import net.darktree.warzone.client.gui.Inset;
import net.darktree.warzone.client.render.color.Color;
import net.darktree.warzone.client.render.color.ImmutableColor;

public class UiLine extends UiComponent {

	private static final Color COLOR = ImmutableColor.of(0, 1, 0);
	private final float thickness;

	protected UiLine(GridContext context, int x, int y, int tx, int ty, float thickness) {
		super(context, x, y, tx, ty, Inset.empty());
		this.thickness = thickness;
	}

	public static Builder of(int x, int y) {
		return new Builder().to(x, y);
	}

	@Override
	public void draw(DrawContext context, GridContext grid) {
		context.drawTiledLine(box.x1, box.y1, box.x2, box.y2, thickness, Sprites.LINE_OVERLAY, 256, Colors.UI_LINE);
		drawDebugOverlay(context, COLOR);
	}

	static public class Builder extends UiComponent.Builder<UiLine, Builder> {

		private int tx = 0;
		private int ty = 0;
		private float thickness = 6;

		public Builder to(int x, int y) {
			this.tx = x;
			this.ty = y;
			return self();
		}

		public Builder width(float thickness) {
			this.thickness = thickness;
			return self();
		}

		@Override
		public Builder inset(float margin) {
			throw new UnsupportedOperationException("Unable to set inset for UiLine!");
		}

		@Override
		public Builder inset(float top, float bottom, float right, float left) {
			throw new UnsupportedOperationException("Unable to set inset for UiLine!");
		}

		@Override
		protected Builder self() {
			return this;
		}

		@Override
		public int getHeight() {
			return 0;
		}

		@Override
		public int getWidth() {
			return 0;
		}

		@Override
		public UiLine build(GridContext context, int x, int y) {
			return new UiLine(context, x, y, tx, ty, thickness);
		}

	}

}
