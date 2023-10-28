package net.darktree.warzone.client.gui.component;

import net.darktree.warzone.client.Colors;
import net.darktree.warzone.client.gui.DrawContext;
import net.darktree.warzone.client.gui.GridContext;
import net.darktree.warzone.client.gui.Inset;
import net.darktree.warzone.client.render.color.Color;
import net.darktree.warzone.client.render.color.ImmutableColor;
import net.darktree.warzone.client.render.image.Sprite;

public class UiIcon extends UiComponent {

	private static final Color COLOR = ImmutableColor.of(0.7f, 0.0f, 0.35f);
	private final Sprite sprite;
	private final Color color;

	protected UiIcon(GridContext context, int x, int y, int width, int height, Inset inset, Sprite sprite, Color color) {
		super(context, x, y, width, height, inset);
		this.sprite = sprite;
		this.color = color;
	}

	public static Builder of(Sprite sprite) {
		return new Builder().sprite(sprite);
	}

	@Override
	public void draw(DrawContext context, GridContext grid) {
		context.drawRect(box.x1, box.y1, box.width(), box.height(), sprite, color);
		drawDebugOverlay(context, COLOR);
	}

	static public class Builder extends UiComponent.Builder<UiIcon, Builder> {

		private Sprite sprite;
		private Color color = Colors.NONE;

		public Builder sprite(Sprite sprite) {
			this.sprite = sprite;
			return self();
		}

		public Builder tint(Color color) {
			this.color = color;
			return self();
		}

		@Override
		protected Builder self() {
			return this;
		}

		@Override
		public UiIcon build(GridContext context, int x, int y) {
			return new UiIcon(context, x, y, width, height, inset, sprite, color);
		}

	}

}

