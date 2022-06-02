package net.darktree.game.screen;

import net.darktree.core.client.Colors;
import net.darktree.core.client.Sprites;
import net.darktree.core.client.render.Alignment;
import net.darktree.core.client.render.Screen;
import net.darktree.core.client.render.ScreenRenderer;
import net.darktree.core.client.render.image.Sprite;
import org.lwjgl.glfw.GLFW;

public class BuildScreen extends Screen {

	private boolean box(int ox, int oy, int w, int h) {
		ScreenRenderer.setOffset(ox, oy);
		return ScreenRenderer.box(w, h);
	}

	private void text(int ox, int oy, String text, Alignment alignment) {
		int x = ScreenRenderer.ox;
		int y = ScreenRenderer.oy;

		ScreenRenderer.setOffset(ox, oy);
		ScreenRenderer.setAlignment(alignment);
		ScreenRenderer.text(text, 30);
		ScreenRenderer.setOffset(x, y);
	}

	private void option(Sprite icon, String name, String description, int value) {
		if (ScreenRenderer.isMouseOver(400, 100)) {
			ScreenRenderer.setColor(Colors.BUTTON_HOVER);
			description(description, value);
		}

		ScreenRenderer.setAlignment(Alignment.LEFT);
		ScreenRenderer.setSprite(icon);
		ScreenRenderer.box(100, 100);
		ScreenRenderer.offset(100, 6);
		ScreenRenderer.text(value + "m", 30);
		ScreenRenderer.offset(0, 54);
		ScreenRenderer.text(name, 30);
		ScreenRenderer.offset(-100, -180);
		ScreenRenderer.setColor(0, 0, 0, 0);
	}

	private void description(String text, int value) {
		text(-100, 120, text + "\n\nCOST: " + value + "m", Alignment.LEFT);
	}

	@Override
	public void draw() {

		ScreenRenderer.centerAt(0, 0);
		ScreenRenderer.setSprite(Sprites.BUILD);

		text(0, 310, "SELECT A BUILDING", Alignment.CENTER);
		text(0, 270, "PAGE 1/2", Alignment.CENTER);
		box(-650, -400, 1300, 800);

		ScreenRenderer.setOffset(-550, 60);

		option(Sprites.BUILDING_CAPITOL, "FACTORY", "THIS WILL APPEAR ON HOVER\nAND DESCRIBE THE FUNCTION\nOF A PARTICULAR BUILDING", 10);
		option(Sprites.BUILDING_CAPITOL, "BOMB FACTORY", "THIS WILL APPEAR ON HOVER\nAND DESCRIBE THE FUNCTION\nOF A PARTICULAR BUILDING", 20);
		option(Sprites.BUILDING_CAPITOL, "SHIELD", "THIS WILL APPEAR ON HOVER\nAND DESCRIBE THE FUNCTION\nOF A PARTICULAR BUILDING", 20);
		option(Sprites.BUILDING_CAPITOL, "LAUNCHER", "THIS WILL APPEAR ON HOVER\nAND DESCRIBE THE FUNCTION\nOF A PARTICULAR BUILDING", 20);

		ScreenRenderer.setOffset(650 - 300, -400 + 100);
		ScreenRenderer.button("<", 1, 50, 50);
		ScreenRenderer.offset(44, 0);
		ScreenRenderer.button(">", 1, 50, 50);

	}

	@Override
	public void onKey(int key, int action, int mods) {
		if (key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_PRESS) {
			this.close();
		}
	}

}
