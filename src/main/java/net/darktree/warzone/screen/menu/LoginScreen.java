package net.darktree.warzone.screen.menu;

import net.darktree.warzone.Main;
import net.darktree.warzone.client.gui.Chain;
import net.darktree.warzone.client.gui.ModelBuilder;
import net.darktree.warzone.client.gui.component.UiButton;
import net.darktree.warzone.client.gui.component.UiNull;
import net.darktree.warzone.client.gui.component.UiText;
import net.darktree.warzone.client.gui.component.UiTextbox;
import net.darktree.warzone.client.gui.prefab.GridContextFactory;
import net.darktree.warzone.client.gui.prefab.GridPrefabs;
import net.darktree.warzone.client.render.Textbox;
import net.darktree.warzone.client.text.Text;

public class LoginScreen extends DecoratedScreen {

	private static final Text TITLE = Text.translated("gui.menu.login.title");
	private static final Text SUBTITLE = Text.translated("gui.menu.login.subtitle");

	private final Textbox username = new Textbox(str -> str.length() <= 15 && !str.trim().isEmpty());


	@Override
	protected GridContextFactory getGridFactory() {
		return GridPrefabs.POPUP;
	}

	@Override
	protected void buildModel(ModelBuilder builder) {
		builder.add(0, 7, UiText.of(TITLE).box(18, 3).center());
		builder.then(Chain.BELOW, UiText.of(SUBTITLE).box(18, 1).center());

		builder.add(1, 1, UiTextbox.of(username).box(12, 2).inset(0.1f, -0.2f).react((prev, next, value) -> {
			if (prev != next) rebuildModel();
		}));

		builder.then(Chain.AFTER, UiNull.of(1, 2));
		builder.then(Chain.AFTER, UiButton.of(TEXT_OK).enabled(username.isValid()).box(3, 2).inset(0.1f, -0.2f).react(() -> {
			// check again just to be sure nothing passes during the transition
			if (username.isValid()) {
				Main.game.setUsername(username.getValue());
				emplace(new MainMenuScreen());
			}
		}));
	}

	@Override
	public void draw(boolean focused) {
		drawDecorBackground();
		drawModel();
	}

	@Override
	public void onEscape() {
		// don't close on ESC
	}

}
