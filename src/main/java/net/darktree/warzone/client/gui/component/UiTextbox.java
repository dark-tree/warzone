package net.darktree.warzone.client.gui.component;

import net.darktree.warzone.client.Colors;
import net.darktree.warzone.client.Sounds;
import net.darktree.warzone.client.Sprites;
import net.darktree.warzone.client.gui.DrawContext;
import net.darktree.warzone.client.gui.GridContext;
import net.darktree.warzone.client.gui.Inset;
import net.darktree.warzone.client.gui.event.TextboxStateListener;
import net.darktree.warzone.client.gui.state.GridState;
import net.darktree.warzone.client.gui.state.Property;
import net.darktree.warzone.client.render.Alignment;
import net.darktree.warzone.client.render.Textbox;
import net.darktree.warzone.client.render.color.Color;
import net.darktree.warzone.client.render.color.ImmutableColor;
import net.darktree.warzone.client.render.image.Sprite;
import net.darktree.warzone.client.sound.Playable;
import net.darktree.warzone.client.window.Window;
import net.darktree.warzone.client.window.input.ClickEvent;
import net.darktree.warzone.client.window.input.Event;
import net.darktree.warzone.client.window.input.KeyEvent;
import net.darktree.warzone.client.window.input.MouseButton;

public class UiTextbox extends UiComponent {

	private static final Color COLOR = ImmutableColor.of(1, 0.1f, 1);
	private final Textbox textbox;
	private final boolean clickable;
	private final float border;
	private final Playable sound;
	private final TextboxStateListener listener;

	protected UiTextbox(GridContext context, int x, int y, int width, int height, Inset inset, boolean clickable, float border, Playable sound, Textbox textbox, TextboxStateListener listener) {
		super(context, x, y, width, height, inset);
		this.clickable = clickable;
		this.border = border;
		this.sound = sound;
		this.textbox = textbox;
		this.listener = listener;
	}

	public static Builder of(Textbox textbox) {
		return new Builder().textbox(textbox);
	}

	private Sprite getBackground(boolean hovered) {
		if (!clickable) return Sprites.BUTTON_DISABLED;

		if (hovered) {
			boolean pressed = Window.getInstance().input().isButtonPressed(MouseButton.LEFT);
			return pressed ? Sprites.BUTTON_PRESSED : Sprites.BUTTON_HOVER;
		}

		return null;
	}

	@Override
	public void change(Property property, boolean enabled) {
		if (property == Property.HOVER) {
			Window.getInstance().setCursor(enabled);
		}
	}

	@Override
	public void react(Event event, GridContext grid) {
		if (!clickable || !shouldAccept(box, event, grid)) {
			return;
		}

		GridState state = grid.getState();

		if (event instanceof ClickEvent ce && ce.hasClicked()) {
			state.set(Property.FOCUS, this, true);

			if (sound != null) {
				sound.play().setVolume(0.45f);
			}
		}

		if (event instanceof KeyEvent ke && state.get(Property.FOCUS, this) && ke.isTyped()) {
			textbox.onKey(listener, ke);
		}
	}

	@Override
	public void draw(DrawContext context, GridContext grid) {
		GridState state = grid.getState();
		boolean hovered = state.get(Property.HOVER, this);
		boolean focused = state.get(Property.FOCUS, this);

		Color line = clickable ? Colors.UI_LINE : Colors.UI_LINE_GRAY;

		float mx = box.x1 + box.width() / 2;
		float my = box.y1 + box.height() / 2;

		context.drawText(mx, my, 30, Alignment.CENTER, textbox.getValue(), line);

		if (border > 0) {
			context.drawLineBox(box, border, Sprites.LINE_OVERLAY, 256, 0, line);
		}

		if (focused) {
			context.drawLineBox(box, 6, Sprites.LINE_OVERLAY, 256, 7f, line);
		}

		Sprite background = getBackground(hovered);
		if (background != null) {
			context.drawTiledRect(box.x1, box.y1, box.width(), box.height(), background, 256, 256, Colors.NONE);
		}

		drawDebugOverlay(context, COLOR);
		state.set(Property.HOVER, this, clickable && grid.isMouseIn(box));
		state.set(Property.FOCUS, this, true); // TODO
	}

	static public class Builder extends UiComponent.Builder<UiTextbox, Builder> {

		private boolean clickable = true;
		private float border = 8;
		private Playable sound = Sounds.SELECT;
		private TextboxStateListener listener = null;
		private Textbox textbox = null;

		public Builder textbox(Textbox textbox) {
			this.textbox = textbox;
			return self();
		}

		public Builder enabled(boolean clickable) {
			this.clickable = clickable;
			return self();
		}

		public Builder disable() {
			return enabled(false);
		}

		public Builder border(float thickness) {
			this.border = thickness;
			return self();
		}

		public Builder sound(Playable sound) {
			this.sound = sound;
			return self();
		}

		public Builder react(TextboxStateListener listener) {
			this.listener = listener;
			return self();
		}

		@Override
		protected Builder self() {
			return this;
		}

		@Override
		public UiTextbox build(GridContext context, int x, int y) {
			return new UiTextbox(context, x, y, width, height, inset, clickable, border, sound, textbox, listener);
		}

	}

}
