package net.darktree.warzone.screen;

import net.darktree.warzone.client.Sprites;
import net.darktree.warzone.client.render.Screen;
import net.darktree.warzone.client.render.ScreenRenderer;

import java.util.function.Consumer;

public class ConfirmScreen extends Screen {

	private final String title;
	private final String message;
	private final Consumer<Boolean> consumer;

	public ConfirmScreen(String title, String message, Consumer<Boolean> consumer) {
		this.title = title;
		this.message = message;
		this.consumer = consumer;
	}

	@Override
	public void draw(boolean focused) {
		drawTitledScreen(title, message, Sprites.POPUP, 610, 340);

		final int buttonParts = 2;
		final int buttonSize = 80;
		final int buttonOffset = 80;

		ScreenRenderer.setOffset(buttonOffset, -120);
		if (ScreenRenderer.button("YES", buttonParts, 38, 80, true)) {
			consumer.accept(true);
			super.close();
		}

		ScreenRenderer.offset(-(buttonParts + 2) * buttonSize / 2 - 2 * buttonOffset, 0);
		if (ScreenRenderer.button("NO", buttonParts, 38, buttonSize, true)) {
			consumer.accept(false);
			super.close();
		}
	}

	@Override
	public void onEscape() {
		consumer.accept(false);
		super.close();
	}

}
