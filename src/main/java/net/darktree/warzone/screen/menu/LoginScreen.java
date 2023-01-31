package net.darktree.warzone.screen.menu;

import net.darktree.warzone.Main;
import net.darktree.warzone.client.Sprites;
import net.darktree.warzone.client.render.ScreenRenderer;
import net.darktree.warzone.client.render.Textbox;
import net.darktree.warzone.client.text.Text;
import org.lwjgl.glfw.GLFW;

public class LoginScreen extends DecoratedScreen {

	private static final Text TITLE = Text.translated("gui.menu.login.title");
	private static final Text SUBTITLE = Text.translated("gui.menu.login.subtitle");

	private final Textbox username = new Textbox(str -> str.length() <= 12 && !str.trim().isEmpty());

	@Override
	public void draw(boolean focused) {
		drawDecorBackground();
		drawTitledScreen(TITLE, SUBTITLE, Sprites.POPUP, 610, 340);

		if (Main.window.input().hasClicked()) {
			username.unselect();
		}

		String value = username.getValue();
		ScreenRenderer.setOffset(140, -140);
		if (ScreenRenderer.button(TEXT_OK, 1, 38, 80, username.valid() && value.length() >= 3)) {
			Main.game.setUsername(value);
			this.emplace(new MainMenuScreen());
		}

		ScreenRenderer.offset(-400, 0);
		username.draw(8, 38, 80);
	}

	@Override
	public void onKey(int key, int action, int mods) {
		super.onKey(key, action, mods);

		if (action == GLFW.GLFW_PRESS || action == GLFW.GLFW_REPEAT) {
			username.onKey(key);
		}
	}

	@Override
	public void onEscape() {

	}

}
