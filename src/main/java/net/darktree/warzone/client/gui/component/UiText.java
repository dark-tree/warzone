package net.darktree.warzone.client.gui.component;

import net.darktree.warzone.client.Colors;
import net.darktree.warzone.client.gui.DrawContext;
import net.darktree.warzone.client.gui.GridContext;
import net.darktree.warzone.client.gui.Inset;
import net.darktree.warzone.client.render.Alignment;
import net.darktree.warzone.client.render.color.Color;
import net.darktree.warzone.client.render.color.ImmutableColor;
import net.darktree.warzone.client.text.Text;

public class UiText extends UiComponent {

	private static final Color COLOR = ImmutableColor.of(1, 0, 0);
	private final String text;
	private final Alignment alignment;
	private final Color tint;

	protected UiText(GridContext context, int x, int y, int width, int height, Inset inset, String text, Alignment alignment, Color tint) {
		super(context, x, y, width, height, inset);
		this.text = text;
		this.alignment = alignment;
		this.tint = tint.immutable();
	}

	public static Builder of(String text) {
		return new Builder().text(text);
	}

	public static Builder of(Text text) {
		return new Builder().text(text.str());
	}

	@Override
	public void draw(DrawContext context, GridContext grid) {
		// subtract not add the offsets as getOffset returns negated values
		float mx = box.x1 - alignment.getOffset(box.width());
		float my = box.y1 + box.height() / 2;

		context.drawText(mx, my, 30, alignment, text, tint);
		drawDebugOverlay(context, COLOR);
	}

	static public class Builder extends UiComponent.Builder<UiText, Builder> {

		private String text = "";
		private Alignment alignment = Alignment.LEFT;
		private Color tint = Colors.TEXT;

		public Builder text(String text) {
			this.text = text;
			return self();
		}

		public Builder tint(Color tint) {
			this.tint = tint;
			return self();
		}

		public Builder align(Alignment alignment) {
			this.alignment = alignment;
			return self();
		}

		public Builder left() {
			return align(Alignment.LEFT);
		}

		public Builder center() {
			return align(Alignment.CENTER);
		}

		public Builder right() {
			return align(Alignment.RIGHT);
		}

		@Override
		protected Builder self() {
			return this;
		}

		@Override
		public UiText build(GridContext context, int x, int y) {
			return new UiText(context, x, y, width, height, inset, text, alignment, tint);
		}

	}

}
