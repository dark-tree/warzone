package net.darktree.warzone.screen;

import net.darktree.warzone.client.Sprites;
import net.darktree.warzone.client.render.Screen;
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
		drawTitledScreen(title, message, Sprites.BUILD, 1300, 800);
	}

	@Override
	public void onKey(int key, int action, int mods) {
		super.onKey(key, action, mods);

		if (key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_PRESS) {
			this.close();
		}
	}

}
