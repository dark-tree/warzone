package net.darktree.warzone.screen;

import net.darktree.warzone.client.Sprites;
import net.darktree.warzone.client.render.Screen;

public class PopupScreen extends Screen {

	private final CharSequence title;
	private final CharSequence message;

	public PopupScreen(CharSequence title, CharSequence message) {
		this.title = title;
		this.message = message;
	}

	@Override
	public void draw(boolean focused) {
		drawTitledScreen(title, message, Sprites.BUILD, 1300, 800);
	}

}
