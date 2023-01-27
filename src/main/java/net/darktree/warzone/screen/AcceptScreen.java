package net.darktree.warzone.screen;

import net.darktree.warzone.client.Sprites;
import net.darktree.warzone.client.render.Screen;
import net.darktree.warzone.client.render.ScreenRenderer;

public class AcceptScreen extends Screen {

	private final CharSequence title;
	private final CharSequence message;

	public AcceptScreen(CharSequence title, CharSequence message) {
		this.title = title;
		this.message = message;
	}

	@Override
	public void draw(boolean focused) {
		drawTitledScreen(title, message, Sprites.POPUP, 610, 340);
		drawButtons();
	}

	protected void drawButtons() {
		ScreenRenderer.setOffset(80, -120);
		if (ScreenRenderer.button(TEXT_OK, 2, 38, 80, true)) {
			super.close();
		}
	}

}