package net.darktree.warzone.screen.menu;

import net.darktree.warzone.Main;
import net.darktree.warzone.client.Colors;
import net.darktree.warzone.client.Sprites;
import net.darktree.warzone.client.render.ScreenRenderer;
import net.darktree.warzone.client.text.Text;
import net.darktree.warzone.client.window.input.KeyEvent;
import net.darktree.warzone.screen.ConfirmScreen;
import net.darktree.warzone.screen.ScreenStack;
import org.lwjgl.glfw.GLFW;

public class MainMenuScreen extends DecoratedScreen {

	private final static Text TEXT_CONFIRM = Text.translated("gui.menu.main.quit.title");

	public MainMenuScreen() {
		//SoundSystem.startMusic(Sounds.MUSIC_HARMONY);
	}

	@Override
	public void draw(boolean focused) {
		int sw = 2 * Main.window.width();
		int sh = 2 * Main.window.height();

		int wp = sw / 7;
		int bw = Math.min((int) (wp * 1.45f), 600);

		drawDecorBackground();
		ScreenRenderer.setColor(Colors.NONE);
		ScreenRenderer.centerAt(-1, 1);
		ScreenRenderer.setOffset(wp, 0);

		for (int i = 0; i < sh / bw + 2; i ++) {
			drawBarSlice(bw, i == 1);
		}

		ScreenRenderer.setColor(Colors.TEXT);
		ScreenRenderer.centerAt(-1, 0);
		ScreenRenderer.setOffset(wp + bw / 2 - 40 * 3 - 1, 100);
		if (ScreenRenderer.button("START", 4, 38, 80, true)) {
			this.emplace(new FreeplayMenuScreen());
		}

		ScreenRenderer.offset(0, -100);
		if (ScreenRenderer.button("QUIT", 4, 38, 80, true) ) {
			tryClose();
		}

		ScreenRenderer.centerAt(-1, -1);
		ScreenRenderer.setOffset(wp + 9, 4);
		ScreenRenderer.text(20, "v1.0");
	}

	public void drawBarSlice(int a, boolean first) {
		ScreenRenderer.setSprite(first ? Sprites.MENU_BAR_TOP : Sprites.MENU_BAR);
		ScreenRenderer.box(a, a);
		ScreenRenderer.offset(0, -a);
	}

	@Override
	public void onKey(KeyEvent event) {
		super.onKey(event);

		if (event.isPressed(GLFW.GLFW_KEY_R)) {
			reloadBackground();
		}
	}

	@Override
	public void onEscape() {
		tryClose();
	}

	private void tryClose() {
		ScreenStack.open(new ConfirmScreen(TEXT_CONFIRM, Text.EMPTY).onYes(Main.window::quit));
	}

}
