package net.darktree.warzone.screen.menu;

import net.darktree.warzone.Main;
import net.darktree.warzone.client.Colors;
import net.darktree.warzone.client.Sounds;
import net.darktree.warzone.client.Sprites;
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

	void drawEntry(WorldSave entry, boolean clicked) {
		ScreenRenderer.setColor(Colors.TEXT);
		ScreenRenderer.text(30, entry.getName());
		ScreenRenderer.offset(0, -30);
		ScreenRenderer.setColor(Colors.PRICE_TAG);
		ScreenRenderer.text(30, TEXT_MAP.str(entry.getCode(), entry.getTime(FORMAT)));

		ScreenRenderer.setColor(Colors.MAP_LIST_DEFAULT);
		ScreenRenderer.setSprite(Sprites.NONE);
		ScreenRenderer.offset(-10, -10);

		if (ScreenRenderer.isMouseOver(540 * 2 + 20, 80)) {
			ScreenRenderer.setColor(Colors.MAP_LIST_HOVER);
			if (clicked) selected = entry;
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
		drawTitledScreen(TEXT_TITLE, getPageString(), Sprites.BUILD, 1300, 800);

		ScreenRenderer.setOffset(-540, 180);
		ScreenRenderer.setAlignment(Alignment.LEFT);

		ScreenRenderer.push();
		ScreenRenderer.setColor(Colors.BORDER);
		ScreenRenderer.offset(-40, 50);
		ScreenRenderer.line(0.005f, 540 * 2 + 80, 0);
		ScreenRenderer.pop();

		// draw the map list
		drawEntryList(Main.window.input().hasClicked());

		ScreenRenderer.offset(-40, 40 - 30 * 3 * 5);
		ScreenRenderer.setColor(Colors.BORDER);
		ScreenRenderer.line(0.005f, 540 * 2 + 80, 0);

		ScreenRenderer.offset(0, -110);
		ScreenRenderer.setColor(Colors.NONE);
		ScreenRenderer.push();
		if (ScreenRenderer.button(TEXT_ENTER, 4, 35, 70, selected != null)) {
			if (!selected.load()) {
				ScreenStack.open(new AcceptScreen(TEXT_LOAD_ERROR, Text.EMPTY));
			}
		}

		ScreenRenderer.offset(250, 0);
		if (ScreenRenderer.button(TEXT_DELETE, 4, 35, 70, selected != null)) {
			ScreenStack.open(new ConfirmScreen(TEXT_DELETE_CONFIRM, Text.EMPTY).onYes(selected::delete));
		}

		ScreenRenderer.offset(250, 0);
		ScreenRenderer.button(TEXT_CREATE, 4, 35, 70, false);
		ScreenRenderer.pop();

		ScreenRenderer.offset(1020, 0);
		if (ScreenRenderer.button(Sprites.BUTTON_LEFT, 64, 64, page > 0)) {
			Sounds.PAGE.play();
			selected = null;
			page --;
		}

		ScreenRenderer.offset(70, 0);
		if (ScreenRenderer.button(Sprites.BUTTON_RIGHT, 64, 64, page < (pages - 1))) {
			Sounds.PAGE.play();
			selected = null;
			page ++;
		}
	}

	@Override
	public void onEscape() {
		this.emplace(new MainMenuScreen());
	}

	protected String getPageString() {
		return TEXT_PAGE.str(page + 1, pages);
	}

}
