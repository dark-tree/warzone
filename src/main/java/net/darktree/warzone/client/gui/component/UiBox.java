package net.darktree.warzone.client.gui.component;

import net.darktree.warzone.client.Colors;
import net.darktree.warzone.client.Sprites;
import net.darktree.warzone.client.gui.DrawContext;
import net.darktree.warzone.client.gui.GridContext;
import net.darktree.warzone.client.gui.Inset;
import net.darktree.warzone.client.render.color.Color;
import net.darktree.warzone.client.render.color.ImmutableColor;
import net.darktree.warzone.client.render.image.Sprite;

public class UiBox extends UiComponent {

	private static final Color COLOR = ImmutableColor.of(1, 1, 0);
	private final float thickness;
	private final Color tint;
	private final Sprite tile;

	protected UiBox(GridContext context, int x, int y, int width, int height, Inset inset, float thickness, Color tint, Sprite tile) {
		super(context, x, y, width, height, inset);
		this.thickness = thickness;
		this.tint = tint.immutable();
		this.tile = tile;
	}

	public static Builder of(int w, int h) {
		return new Builder().box(w, h);
	}

	@Override
	public void draw(DrawContext context, GridContext grid) {

		if (thickness > 0) {
			context.drawLineBox(box, thickness, Sprites.LINE_OVERLAY, 256, 0, tint);
		}

		if (tile != null) {
			context.drawTiledRect(box.x1, box.y1, box.width(), box.height(), tile, 256, 256, Colors.NONE);
		}

		drawDebugOverlay(context, COLOR);
	}

	static public class Builder extends UiComponent.Builder<UiBox, Builder> {

		private Color tint = Colors.UI_LINE;
		private float thickness = 8;
		private Sprite tile = null;

		public Builder width(float thickness) {
			this.thickness = thickness;
			return self();
		}

		public Builder tint(Color tint) {
			this.tint = tint;
			return self();
		}

		public Builder tile(Sprite tile) {
			this.tile = tile;
			return self();
		}

		@Override
		protected Builder self() {
			return this;
		}

		@Override
		public UiBox build(GridContext context, int x, int y) {
			return new UiBox(context, x, y, width, height, inset, thickness, tint, tile);
		}

	}

}
