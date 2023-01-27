package net.darktree.warzone.screen.menu;

import net.darktree.warzone.client.Sprites;
import net.darktree.warzone.client.render.Screen;
import net.darktree.warzone.client.render.ScreenRenderer;
import net.darktree.warzone.client.text.Text;
import net.darktree.warzone.screen.AcceptScreen;
import net.darktree.warzone.screen.ScreenStack;
import net.darktree.warzone.world.World;
import net.darktree.warzone.world.WorldSave;

public class PauseMenuScreen extends Screen {

	private static final Text TEXT_TITLE = Text.translated("gui.menu.pause.title");
	private static final Text TEXT_EXIT = Text.translated("gui.menu.pause.exit");
	private static final Text TEXT_SETTINGS = Text.translated("gui.menu.pause.settings");
	private static final Text TEXT_SAVE_ERROR = Text.translated("gui.menu.pause.save.error");

	private final WorldSave save;
	private final World world;

	public PauseMenuScreen(WorldSave save, World world) {
		this.save = save;
		this.world = world;
	}

	@Override
	public void draw(boolean focused) {
		drawTitledScreen(TEXT_TITLE, Text.EMPTY, Sprites.PAUSE, 800, 400);
		final int width = 9;

		ScreenRenderer.setOffset((int) (- 40 * (width / 2.0f + 1) - 1), -40);
		if (ScreenRenderer.button(TEXT_EXIT, width, 38, 80, true)) {

			if (save.save(world)) {
				ScreenStack.closeAll();
				ScreenStack.open(new FreeplayMenuScreen());
				return;
			}

			ScreenStack.open(new AcceptScreen(TEXT_SAVE_ERROR, Text.EMPTY));
		}

		ScreenRenderer.offset(0, -100);
		ScreenRenderer.button(TEXT_SETTINGS, width, 38, 80, false);
	}

}
