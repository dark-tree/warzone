package net.darktree.warzone.screen.menu;

import net.darktree.warzone.client.Colors;
import net.darktree.warzone.client.Sprites;
import net.darktree.warzone.client.gui.Chain;
import net.darktree.warzone.client.gui.ModelBuilder;
import net.darktree.warzone.client.gui.component.UiButton;
import net.darktree.warzone.client.gui.component.UiIcon;
import net.darktree.warzone.client.gui.component.UiNull;
import net.darktree.warzone.client.gui.component.UiText;
import net.darktree.warzone.client.gui.prefab.GridContextFactory;
import net.darktree.warzone.client.gui.prefab.GridPrefabs;
import net.darktree.warzone.client.render.Screen;
import net.darktree.warzone.client.text.Text;
import net.darktree.warzone.screen.AcceptScreen;
import net.darktree.warzone.screen.ComposedScreen;
import net.darktree.warzone.screen.ScreenStack;
import net.darktree.warzone.world.WorldAccess;
import net.darktree.warzone.world.WorldSave;

public class PauseMenuScreen extends Screen {

	private static final Text TEXT_TITLE = Text.translated("gui.menu.pause.title");
	private static final Text TEXT_EXIT = Text.translated("gui.menu.pause.exit");
	private static final Text TEXT_SETTINGS = Text.translated("gui.menu.pause.settings");
	private static final Text TEXT_SAVE_ERROR = Text.translated("gui.menu.pause.save.error");

	private final WorldSave save;
	private final WorldAccess world;

	@Override
	protected GridContextFactory getGridFactory() {
		return GridPrefabs.POPUP;
	}

	public PauseMenuScreen(WorldSave save, WorldAccess world) {
		this.save = save;
		this.world = world;
	}

	@Override
	protected void buildModel(ModelBuilder builder) {

		// decal
		builder.add(-1, 1, UiIcon.of(Sprites.DECAL_SPLAT).box(10, 10));

		// title
		builder.add(0, 7, UiText.of(TEXT_TITLE).box(18, 3).center());

		// append buttons
		builder.add(4, 1, UiButton.of(TEXT_SETTINGS).disable().box(10, 2).inset(0.1f, -0.2f));
		builder.then(Chain.ABOVE, UiNull.of(10, 1));
		builder.then(Chain.ABOVE, UiButton.of(TEXT_EXIT).box(10, 2).inset(0.1f, -0.2f).react(() -> {
			if (save.save(world)) {
				ScreenStack.closeAll();
				ScreenStack.open(new ComposedScreen(new DecoratedScreen(), new FreeplayMenuScreen()));
				return;
			}

			ScreenStack.open(new AcceptScreen(TEXT_SAVE_ERROR, Text.EMPTY));
		}));
	}

	@Override
	public void draw(boolean focused) {
		drawBackground(Colors.SCREEN_SEPARATOR);
		drawModel();
	}

}
