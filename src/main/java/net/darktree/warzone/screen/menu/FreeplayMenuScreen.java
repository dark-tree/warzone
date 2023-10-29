package net.darktree.warzone.screen.menu;

import net.darktree.warzone.Main;
import net.darktree.warzone.client.Colors;
import net.darktree.warzone.client.Sprites;
import net.darktree.warzone.client.gui.Chain;
import net.darktree.warzone.client.gui.GridContext;
import net.darktree.warzone.client.gui.ModelBuilder;
import net.darktree.warzone.client.gui.component.UiButton;
import net.darktree.warzone.client.gui.component.UiLine;
import net.darktree.warzone.client.gui.component.UiNull;
import net.darktree.warzone.client.gui.component.UiText;
import net.darktree.warzone.client.render.Alignment;
import net.darktree.warzone.client.render.ScreenRenderer;
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
	}

	void drawEntry(WorldSave entry, boolean clicked) {
		ScreenRenderer.setColor(Colors.TEXT);
		ScreenRenderer.text(30, entry.getName());
		ScreenRenderer.offset(0, -30);
		ScreenRenderer.setColor(Colors.PRICE_TAG);
		ScreenRenderer.text(30, TEXT_MAP.str(entry.getCode(), entry.getTime(FORMAT)));

		ScreenRenderer.setColor(Colors.MAP_LIST_DEFAULT);
		ScreenRenderer.setSprite(Sprites.BLANK);
		ScreenRenderer.offset(-10, -10);

		if (ScreenRenderer.isMouseOver(540 * 2 + 20, 80)) {
			ScreenRenderer.setColor(Colors.MAP_LIST_HOVER);
			if (clicked) {
				selected = entry;
				rebuildModel();
			}
		}

		if (selected == entry) {
			ScreenRenderer.setColor(Colors.MAP_LIST_SELECTED);
		}

		ScreenRenderer.box(540 * 2 + 20, 80);
		ScreenRenderer.offset(10, -20);
	}

	protected void drawEntryList(boolean clicked) {
		final int start = this.page * 5;
		final int end = Math.min(start + 5, saves.size());

		ScreenRenderer.push();
		for (int i = start; i < end; i ++) {
			drawEntry(saves.get(i), clicked);
			ScreenRenderer.offset(0, -30);
		}
		ScreenRenderer.pop();
	}

	@Override
	public void draw(boolean focused) {
		drawDecorBackground();
		drawModel();

		// draw the map list
		ScreenRenderer.setOffset(-540, 180);
		ScreenRenderer.setAlignment(Alignment.LEFT);
		drawEntryList(Main.window.input().hasClicked());
	}

	@Override
	public void onEscape() {
		this.emplace(new MainMenuScreen());
	}

	protected String getPageString() {
		return TEXT_PAGE.str(page + 1, pages);
	}

}
