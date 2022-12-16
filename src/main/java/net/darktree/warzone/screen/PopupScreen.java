package net.darktree.warzone.screen;

import net.darktree.warzone.Main;
import net.darktree.warzone.client.Colors;
import net.darktree.warzone.client.Sprites;
import net.darktree.warzone.client.render.Alignment;
import net.darktree.warzone.client.render.Screen;
import net.darktree.warzone.client.render.ScreenRenderer;
import org.lwjgl.glfw.GLFW;

public class PopupScreen extends Screen {

	private final String title;
	private final String message;

	public PopupScreen(String title, String message) {
		this.title = title;
		this.message = message;
	}

	@Override
	public void draw(boolean focused) {

		ScreenRenderer.centerAt(-1, -1);
		ScreenRenderer.setSprite(Sprites.NONE);
		ScreenRenderer.setColor(Colors.SCREEN_SEPARATOR);
		ScreenRenderer.box(Main.window.width() * 2, Main.window.height() * 2);

		ScreenRenderer.centerAt(0, 0);
		ScreenRenderer.setColor(Colors.NONE);
		ScreenRenderer.setSprite(Sprites.BUILD);

		text(0, 310, title, Alignment.CENTER);
		text(0, 270, message, Alignment.CENTER);
		box(-650, -400, 1300, 800);

	}

	@Override
	public void onKey(int key, int action, int mods) {
		super.onKey(key, action, mods);

		if (key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_PRESS) {
			this.close();
		}
	}

}
