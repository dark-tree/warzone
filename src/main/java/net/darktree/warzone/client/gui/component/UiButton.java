package net.darktree.warzone.client.gui.component;

import net.darktree.warzone.client.Colors;
import net.darktree.warzone.client.Sounds;
import net.darktree.warzone.client.Sprites;
import net.darktree.warzone.client.gui.DrawContext;
import net.darktree.warzone.client.gui.GridContext;
import net.darktree.warzone.client.gui.Inset;
import net.darktree.warzone.client.gui.event.ButtonClickListener;
import net.darktree.warzone.client.gui.state.GridState;
import net.darktree.warzone.client.gui.state.Property;
import net.darktree.warzone.client.render.Alignment;
import net.darktree.warzone.client.render.color.Color;
import net.darktree.warzone.client.render.color.ImmutableColor;
import net.darktree.warzone.client.render.image.Sprite;
import net.darktree.warzone.client.sound.Playable;
import net.darktree.warzone.client.window.Window;
import net.darktree.warzone.client.window.input.ClickEvent;
import net.darktree.warzone.client.window.input.Event;
import net.darktree.warzone.client.window.input.KeyEvent;
import net.darktree.warzone.client.window.input.MouseButton;
import org.lwjgl.glfw.GLFW;

public class UiButton extends UiComponent {

	private static final Color COLOR = ImmutableColor.of(1, 0.5f, 0.5f);
	private final boolean clickable;
	private final float border;
	private final Sprite sprite;
	private final String text;
	private final Playable sound;
	private final ButtonClickListener listener;

	protected UiButton(GridContext context, int x, int y, int width, int height, Inset inset, boolean clickable, float border, Sprite sprite, String text, Playable sound, ButtonClickListener listener) {
		super(context, x, y, width, height, inset);
		this.clickable = clickable;
		this.border = border;
		this.sprite = sprite;
		this.text = text;
		this.sound = sound;
		this.listener = listener;
	}

	public static Builder of(Sprite sprite) {
		return new Builder().sprite(sprite);
	}

	public static Builder of(String text) {
		return new Builder().text(text);
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
		if (!clickable || !shouldAccept(event, grid)) {
			return;
		}

		boolean mouse = event instanceof ClickEvent ce && ce.hasClicked();
		boolean key = event instanceof KeyEvent ke && ke.key == GLFW.GLFW_KEY_ENTER && ke.isTyped();

		if (mouse || (key && grid.getState().get(Property.FOCUS, this))) {
			if (listener != null) {
				listener.handle();
			}

			if (sound != null) {
				sound.play();
			}
		}
	}

	@Override
	public void draw(DrawContext context, GridContext grid) {
		GridState state = grid.getState();
		boolean hovered = state.get(Property.HOVER, this);
		boolean focused = state.get(Property.FOCUS, this);

		Color line = clickable ? Colors.UI_LINE : Colors.UI_LINE_GRAY;

		if (sprite != null) {
			context.drawRect(box.x1, box.y1, box.width(), box.height(), sprite, clickable ? Colors.NONE : Colors.UI_ICON_GRAY);
		}

		if (text != null) {
			float mx = box.x1 + box.width() / 2;
			float my = box.y1 + box.height() / 2;

			context.drawText(mx, my, 30, Alignment.CENTER, text, line);
		}

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
	}

	static public class Builder extends UiComponent.Builder<UiButton, Builder> {

		private boolean clickable = true;
		private float border = 8;
		private Sprite sprite = null;
		private String text = null;
		private Playable sound = Sounds.SELECT;
		private ButtonClickListener listener;

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

		public Builder sprite(Sprite sprite) {
			this.sprite = sprite;
			return self();
		}

		public Builder text(String text) {
			this.text = text;
			return self();
		}

		public Builder react(ButtonClickListener listener) {
			this.listener = listener;
			return self();
		}

		public Builder sound(Playable sound) {
			this.sound = sound;
			return self();
		}

		@Override
		protected Builder self() {
			return this;
		}

		@Override
		public UiButton build(GridContext context, int x, int y) {
			return new UiButton(context, x, y, width, height, inset, clickable, border, sprite, text, sound, listener);
		}

	}

}
