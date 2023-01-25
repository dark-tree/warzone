package net.darktree.warzone.screen.menu;

import net.darktree.warzone.Main;
import net.darktree.warzone.client.Colors;
import net.darktree.warzone.client.Sounds;
import net.darktree.warzone.client.Sprites;
import net.darktree.warzone.client.render.Alignment;
import net.darktree.warzone.client.render.ScreenRenderer;
import net.darktree.warzone.world.SaveState;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class FreeplayMenuScreen extends DecoratedScreen {

	private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

	List<SaveState> saves;

	FreeplayMenuScreen() {
		saves = Main.game.getSaves();
		pages = (int) Math.ceil(saves.size() / 5.0f);
	}

	protected int page;
	protected int pages;
	SaveState active;

	void drawEntry(SaveState entry, boolean clicked) {
		ScreenRenderer.setColor(Colors.NONE);
		ScreenRenderer.text(30, entry.getName());
		ScreenRenderer.offset(0, -30);
		ScreenRenderer.setColor(Colors.PRICE_TAG);
		ScreenRenderer.text(30, "MAP: " + entry.getCode() + " (" + entry.getTime(FORMAT) + ")");

		ScreenRenderer.setColor(Colors.MAP_LIST_DEFAULT);
		ScreenRenderer.setSprite(Sprites.NONE);
		ScreenRenderer.offset(-10, -10);

		if (ScreenRenderer.isMouseOver(540 * 2 + 20, 80)) {
			ScreenRenderer.setColor(Colors.MAP_LIST_HOVER);

			if (clicked) {
				active = entry;
			}
		}

		if (active == entry) {
			ScreenRenderer.setColor(Colors.MAP_LIST_SELECTED);
		}

		ScreenRenderer.box(540 * 2 + 20, 80);
		ScreenRenderer.offset(10, -20);
	}

	protected void drawEntryList(int size, boolean clicked) {
		final int start = this.page * size;
		final int end = Math.min(start + size, saves.size());

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
		drawTitledScreen("CREATE OR SELECT AN ACTIVE MAP", getPageString(), Sprites.BUILD, 1300, 800);

//		ScreenRenderer.centerAt();

		ScreenRenderer.setOffset(-540, 180);
		ScreenRenderer.setAlignment(Alignment.LEFT);

		ScreenRenderer.push();
		ScreenRenderer.setColor(Colors.BORDER);
		ScreenRenderer.offset(-40, 50);
		ScreenRenderer.line(0.005f, 540 * 2 + 80, 0);
		ScreenRenderer.pop();

		// draw the map list
		drawEntryList(5, Main.window.input().hasClicked());

		ScreenRenderer.offset(-40, 40 - 30 * 3 * 5);
		ScreenRenderer.setColor(Colors.BORDER);
		ScreenRenderer.line(0.005f, 540 * 2 + 80, 0);

//		ScreenRenderer.setOffset(-550, -300);
		ScreenRenderer.offset(0, -110);

		ScreenRenderer.setColor(Colors.NONE);
		ScreenRenderer.push();
		if (ScreenRenderer.button("ENTER", 4, 35, 70, active != null)) {
			active.load();
		}

		ScreenRenderer.offset(250, 0);
		ScreenRenderer.button("DELETE", 4, 35, 70, active != null);

		ScreenRenderer.offset(250, 0);
		ScreenRenderer.button("CREATE", 4, 35, 70, true);
		ScreenRenderer.pop();

		ScreenRenderer.offset(1020, 0);
		if (ScreenRenderer.button(Sprites.BUTTON_LEFT, 64, 64, page > 0)) {
			Sounds.PAGE.play();
			active = null;
			page --;
		}

		ScreenRenderer.offset(70, 0);
		if (ScreenRenderer.button(Sprites.BUTTON_RIGHT, 64, 64, page < (pages - 1))) {
			Sounds.PAGE.play();
			active = null;
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
