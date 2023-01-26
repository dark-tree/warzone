package net.darktree.warzone.screen.menu;

import net.darktree.warzone.client.Sprites;
import net.darktree.warzone.client.render.Screen;
import net.darktree.warzone.client.render.ScreenRenderer;
import net.darktree.warzone.client.text.Text;
import net.darktree.warzone.screen.ScreenStack;

public class PauseMenuScreen extends Screen {

	@Override
	public void draw(boolean focused) {
		drawTitledScreen("GAME MENU", Text.EMPTY, Sprites.PAUSE, 800, 400);
		final int width = 9;

		ScreenRenderer.setOffset((int) (- 40 * (width / 2.0f + 1) - 1), -40);
		if (ScreenRenderer.button("SAVE AND EXIT", width, 38, 80, true)) {
			ScreenStack.closeAll();
			ScreenStack.open(new FreeplayMenuScreen());
		}

		ScreenRenderer.offset(0, -100);
		ScreenRenderer.button("SETTINGS", width, 38, 80, false);
	}

}
