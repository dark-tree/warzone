package net.darktree.warzone.screen.menu;

import net.darktree.warzone.Main;
import net.darktree.warzone.client.Colors;
import net.darktree.warzone.client.Sounds;
import net.darktree.warzone.client.Sprites;
import net.darktree.warzone.client.gui.Chain;
import net.darktree.warzone.client.gui.ComponentBuilder;
import net.darktree.warzone.client.gui.ModelBuilder;
import net.darktree.warzone.client.gui.component.*;
import net.darktree.warzone.client.gui.prefab.GridContextFactory;
import net.darktree.warzone.client.gui.prefab.GridPrefabs;
import net.darktree.warzone.client.render.color.ImmutableColor;
import net.darktree.warzone.client.render.image.Sprite;
import net.darktree.warzone.client.sound.Playable;
import net.darktree.warzone.client.text.Text;
import net.darktree.warzone.screen.*;
import net.darktree.warzone.world.WorldSave;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class FreeplayMenuScreen extends PaginatedScreen {

	public static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

	private final static Text TEXT_TITLE = Text.translated("gui.menu.freeplay.title");
	private final static Text TEXT_ENTER = Text.translated("gui.menu.freeplay.enter");
	private final static Text TEXT_DELETE = Text.translated("gui.menu.freeplay.delete");
	private final static Text TEXT_CREATE = Text.translated("gui.menu.freeplay.create");
	private final static Text TEXT_MAP = Text.translated("gui.menu.freeplay.subtitle");
	private final static Text TEXT_DELETE_CONFIRM = Text.translated("gui.menu.freeplay.delete.confirm");
	private final static Text TEXT_LOAD_ERROR = Text.translated("gui.menu.freeplay.load.error");

	private final List<WorldSave> saves;
	private WorldSave selected;

	public FreeplayMenuScreen() {
		this.saves = Main.game.getSaves();
		setPagination(saves.size(), 5);
	}

	@Override
	protected GridContextFactory getGridFactory() {
		return GridPrefabs.FREEPLAY;
	}

	@Override
	protected void buildModel(ModelBuilder builder) {

		// decals
		builder.add(25, 1, UiIcon.of(Sprites.DECAL_SPLAT).box(10, 10));
		builder.add(22, 12, UiIcon.of(Sprites.DECAL_MUG).box(10, 10));
		builder.add(0, 0, UiIcon.of(Sprites.DECAL_BLOB).tint(ImmutableColor.of(1, 1, 1, 0.5f)).box(23, 4));

		// title
		builder.add(0, 22, UiText.of(TEXT_TITLE).box(39, 2).center());
		builder.then(Chain.BELOW, UiText.of(getPageString()).box(39, 1).center());

		// page buttons
		buildPaginatedModel(builder);

		// top & bottom line
		builder.add(1, 20, UiLine.of(37, 0));
		builder.add(1, 4, UiLine.of(37, 0));

		// enter button
		builder.add(1, 1, UiNull.of(1, 2));
		builder.then(Chain.AFTER, UiButton.of(TEXT_ENTER).enabled(selected != null).inset(0.1f, -0.2f).box(6, 2).react(() -> {
			if (!selected.load()) ScreenStack.open(new AcceptScreen(TEXT_LOAD_ERROR, Text.EMPTY));
		}));

		// delete button
		builder.then(Chain.AFTER, UiNull.of(1, 2));
		builder.then(Chain.AFTER, UiButton.of(TEXT_DELETE).enabled(selected != null).inset(0.1f, -0.2f).box(6, 2).react(() -> {
			ScreenStack.open(new ConfirmScreen(TEXT_DELETE_CONFIRM, Text.EMPTY).onYes(selected::delete));
		}));

		// create button
		builder.then(Chain.AFTER, UiNull.of(1, 2));
		builder.then(Chain.AFTER, UiButton.of(TEXT_CREATE).disable().inset(0.1f, -0.2f).box(6, 2));

		// draw map list
		builder.add(3, 19);
		for (WorldSave save : getPaged(saves)) {
			builder.then(Chain.BELOW, buildEntryModel(save));
			builder.then(Chain.BELOW, UiNull.of(33, 1));
		}
	}

	ComponentBuilder<UiComposed> buildEntryModel(WorldSave save) {
		var builder = UiComposed.of();

		Sprite background = selected == save ? Sprites.BUTTON_PRESSED : Sprites.BUTTON_HOVER;
		Playable press = selected == save ? null : Sounds.HATCH;

		builder.add(0, 0, UiBox.of(33, 2).inset(-0.3f).tile(background));
		builder.add(0, 1, UiText.of(save.getName()).box(33, 1).left().tint(Colors.TEXT));
		builder.add(0, 0, UiText.of(TEXT_MAP.str(save.getCode(), save.getTime(FORMAT))).box(33, 1).left().tint(Colors.PRICE_TAG));
		builder.add(0, 0, UiButton.of().box(33, 2).sound(press).inset(-0.3f).react(() -> {
			selected = save;
			rebuildModel();
		}));

		return builder;
	}

	@Override
	public void draw(boolean focused) {
		drawModel();
	}

	@Override
	public void onEscape() {
		emplace(new ComposedScreen(new DecoratedScreen(), new MainMenuScreen()));
	}

}
