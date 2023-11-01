package net.darktree.warzone.screen.menu;

import net.darktree.warzone.client.gui.Chain;
import net.darktree.warzone.client.gui.GridContext;
import net.darktree.warzone.client.gui.ModelBuilder;
import net.darktree.warzone.client.gui.component.UiButton;
import net.darktree.warzone.client.gui.component.UiNull;
import net.darktree.warzone.client.gui.component.UiText;
import net.darktree.warzone.client.render.Screen;
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

	@Override
	protected GridContext createGridContext() {
		return new GridContext(22, 10, GridContext.SIZE);
	}

	public PauseMenuScreen(WorldSave save, World world) {
		this.save = save;
		this.world = world;
	}

	@Override
	protected void buildModel(ModelBuilder builder) {
		builder.add(0, 7, UiText.of(TEXT_TITLE).box(22, 3).center());

		// append buttons
		builder.add(5, 1, UiButton.of(TEXT_SETTINGS).disable().box(12, 2).inset(0.1f, -0.2f));
		builder.then(Chain.ABOVE, UiNull.of(12, 1));
		builder.then(Chain.ABOVE, UiButton.of(TEXT_EXIT).box(12, 2).inset(0.1f, -0.2f).react(() -> {
			if (save.save(world)) {
				ScreenStack.closeAll();
				ScreenStack.open(new FreeplayMenuScreen());
				return;
			}

			ScreenStack.open(new AcceptScreen(TEXT_SAVE_ERROR, Text.EMPTY));
		}));
	}

	@Override
	public void draw(boolean focused) {
		drawModel();
	}

}
