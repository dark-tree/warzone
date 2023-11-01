package net.darktree.warzone.screen.menu;

import net.darktree.warzone.Main;
import net.darktree.warzone.client.Colors;
import net.darktree.warzone.client.Sounds;
import net.darktree.warzone.client.Sprites;
import net.darktree.warzone.client.gui.Chain;
import net.darktree.warzone.client.gui.ComponentBuilder;
import net.darktree.warzone.client.gui.GridContext;
import net.darktree.warzone.client.gui.ModelBuilder;
import net.darktree.warzone.client.gui.component.*;
import net.darktree.warzone.client.render.image.Sprite;
import net.darktree.warzone.client.sound.buffer.AudioBuffer;
import net.darktree.warzone.client.text.Text;
import net.darktree.warzone.screen.AcceptScreen;
import net.darktree.warzone.screen.ConfirmScreen;
import net.darktree.warzone.screen.ScreenStack;
import net.darktree.warzone.world.WorldSave;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class FreeplayMenuScreen extends DecoratedScreen {

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
	private final int pages;
	private int page;

	public FreeplayMenuScreen() {
		this.saves = Main.game.getSaves();
		this.pages = (int) Math.ceil(saves.size() / 5.0f);
	}

	@Override
	protected GridContext createGridContext() {
		return new GridContext(39, 23, GridContext.SIZE);
	}

	@Override
	protected void buildModel(ModelBuilder builder) {
		// title
		builder.add(0, 21, UiText.of(TEXT_TITLE.str()).box(39, 2).center());
		builder.then(Chain.BELOW, UiText.of(getPageString()).box(39, 1).center());

		// top & bottom line
		builder.add(1, 19, UiLine.of(38, 19));
		builder.add(1, 4, UiLine.of(38, 4));

		// enter button
		builder.add(1, 1, UiNull.of(1, 2));
		builder.then(Chain.AFTER, UiButton.of(TEXT_ENTER.str()).enabled(selected != null).inset(0.1f, -0.2f).box(6, 2).react(() -> {
			if (!selected.load()) ScreenStack.open(new AcceptScreen(TEXT_LOAD_ERROR, Text.EMPTY));
		}));

		// delete button
		builder.then(Chain.AFTER, UiNull.of(1, 2));
		builder.then(Chain.AFTER, UiButton.of(TEXT_DELETE.str()).enabled(selected != null).inset(0.1f, -0.2f).box(6, 2).react(() -> {
			ScreenStack.open(new ConfirmScreen(TEXT_DELETE_CONFIRM, Text.EMPTY).onYes(selected::delete));
		}));

		// create button
		builder.then(Chain.AFTER, UiNull.of(1, 2));
		builder.then(Chain.AFTER, UiButton.of(TEXT_CREATE.str()).disable().inset(0.1f, -0.2f).box(6, 2));

		// page buttons
		builder.add(35, 1, UiButton.of(Sprites.ICON_NEXT).disable().border(0).box(2, 2));
		builder.then(Chain.BEFORE, UiButton.of(Sprites.ICON_PREV).disable().border(0).box(2, 2));

		// draw map list
		for (int i = 0; i < saves.size(); i ++) {
			builder.add(3, 19 - 3 * (i + 1), buildEntryModel(saves.get(i)));
		}
	}

	ComponentBuilder<UiComposed> buildEntryModel(WorldSave save) {
		var builder = UiComposed.of();

		Sprite background = selected == save ? Sprites.BUTTON_PRESSED : Sprites.BUTTON_HOVER;
		AudioBuffer press = selected == save ? null : Sounds.HATCH;

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
		drawDecorBackground();
		drawModel();
	}

	@Override
	public void onEscape() {
		this.emplace(new MainMenuScreen());
	}

	protected String getPageString() {
		return TEXT_PAGE.str(page + 1, pages);
	}

}
