package net.darktree.warzone.screen;

import net.darktree.warzone.client.Colors;
import net.darktree.warzone.client.Sprites;
import net.darktree.warzone.client.gui.Chain;
import net.darktree.warzone.client.gui.ModelBuilder;
import net.darktree.warzone.client.gui.component.UiButton;
import net.darktree.warzone.client.gui.component.UiIcon;
import net.darktree.warzone.client.gui.component.UiText;
import net.darktree.warzone.client.gui.prefab.GridContextFactory;
import net.darktree.warzone.client.gui.prefab.GridPrefabs;
import net.darktree.warzone.client.render.Screen;

public class AcceptScreen extends Screen {

	private final String title;
	private final String message;

	public AcceptScreen(CharSequence title, CharSequence message) {
		this.title = title.toString();
		this.message = message.toString();
	}

	@Override
	protected GridContextFactory getGridFactory() {
		return GridPrefabs.POPUP;
	}

	@Override
	protected void buildModel(ModelBuilder builder) {

		// decals
		builder.add(0, 0, UiIcon.of(Sprites.DECAL_SMUDGE).box(10, 10));
		builder.add(8, 0, UiIcon.of(Sprites.DECAL_SPLAT).box(10, 10));

		// title
		builder.add(0, 7, UiText.of(title).box(18, 3).center());
		builder.then(Chain.BELOW, UiText.of(message).box(18, 1).center());

		buildButtonModel(builder);
	}

	protected void buildButtonModel(ModelBuilder builder) {
		builder.add(6, 1, UiButton.of(TEXT_OKAY).inset(0.1f, -0.2f).box(6, 2).react(this::close));
	}

	@Override
	public void draw(boolean focused) {
		drawBackground(Colors.SCREEN_SEPARATOR);
		drawModel();
	}

}