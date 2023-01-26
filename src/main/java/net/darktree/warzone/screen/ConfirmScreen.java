package net.darktree.warzone.screen;

import net.darktree.warzone.client.Sprites;
import net.darktree.warzone.client.render.Screen;
import net.darktree.warzone.client.render.ScreenRenderer;

public class ConfirmScreen extends Screen {

	private final CharSequence title;
	private final CharSequence message;
	private Runnable yes, no;

	public ConfirmScreen(CharSequence title, CharSequence message) {
		this.title = title;
		this.message = message;
	}

	public ConfirmScreen onYes(Runnable runnable) {
		this.yes = runnable;
		return this;
	}

	public ConfirmScreen onNo(Runnable runnable) {
		this.no = runnable;
		return this;
	}

	@Override
	public void draw(boolean focused) {
		drawTitledScreen(title, message, Sprites.POPUP, 610, 340);

		final int buttonParts = 2;
		final int buttonSize = 80;
		final int buttonOffset = 80;

		ScreenRenderer.setOffset(buttonOffset, -120);
		if (ScreenRenderer.button(TEXT_YES, buttonParts, 38, 80, true)) {
			runAction(true);
			super.close();
		}

		ScreenRenderer.offset(-(buttonParts + 2) * buttonSize / 2 - 2 * buttonOffset, 0);
		if (ScreenRenderer.button(TEXT_NO, buttonParts, 38, buttonSize, true)) {
			runAction(false);
			super.close();
		}
	}

	@Override
	public void onEscape() {
		runAction(false);
		super.close();
	}

	private void runAction(boolean state) {
		if (state && yes != null) yes.run();
		if (!state && no != null) no.run();
	}

}
