package net.darktree.warzone.screen;

import net.darktree.warzone.client.render.Screen;
import net.darktree.warzone.client.window.input.ClickEvent;
import net.darktree.warzone.client.window.input.KeyEvent;

public class ComposedScreen extends Screen {

	private final Screen[] screens;

	public ComposedScreen(Screen... screens) {
		this.screens = screens;
	}

	@Override
	public void draw(boolean focused) {
		for (Screen screen : screens) {

			// all screens in ComposedScreen share focus
			screen.draw(focused);
		}
	}

	@Override
	public void onKey(KeyEvent event) {
		super.onKey(event);

		for (Screen screen : screens) {
			screen.onKey(event);
		}
	}

	@Override
	public void onClick(ClickEvent event) {
		super.onClick(event);

		for (Screen screen : screens) {
			screen.onClick(event);
		}
	}

	@Override
	public void onScroll(float value) {
		super.onScroll(value);

		for (Screen screen : screens) {
			screen.onScroll(value);
		}
	}

	@Override
	public void onMove(float x, float y) {
		super.onMove(x, y);

		for (Screen screen : screens) {
			screen.onMove(x, y);
		}
	}

	@Override
	public void onResize(int w, int h) {
		super.onResize(w, h);

		for (Screen screen : screens) {
			screen.onResize(w, h);
		}
	}

	@Override
	public void onRemoved() {
		super.onRemoved();

		for (Screen screen : screens) {
			screen.onRemoved();
		}
	}

	@Override
	public void onEscape() {
		// don't close composed screen on ESC,
		// we don't need to pass this event, it is derived
		// from onKey by the child screens
	}

	@Override
	public boolean isClosed() {
		for (Screen screen : screens) {
			if (screen.isClosed()) return true;
		}

		return super.isClosed();
	}

}
