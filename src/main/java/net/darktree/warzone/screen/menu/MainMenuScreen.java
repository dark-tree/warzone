package net.darktree.warzone.screen.menu;

import net.darktree.warzone.Main;
import net.darktree.warzone.client.Colors;
import net.darktree.warzone.client.Sprites;
import net.darktree.warzone.client.render.Screen;
import net.darktree.warzone.client.render.ScreenRenderer;
import net.darktree.warzone.screen.PlayScreen;
import net.darktree.warzone.screen.ScreenStack;
import net.darktree.warzone.util.Logger;
import net.darktree.warzone.world.WorldHolder;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class MainMenuScreen extends Screen {

	private final List<DecorCard> cards = new ArrayList<>();
	private final CardSource source = new CardSource(8);

	public MainMenuScreen() {
		init();
	}

	public void onResize(int w, int h) {
		init();
	}

	void init() {
		float scale = Main.window.scale();
		long start = System.currentTimeMillis();
		cards.clear();
		fillVisible(2 * Main.window.width(), 2 * Main.window.height(), scale, 8);
		Logger.info("MainMenuScreen took ", System.currentTimeMillis() - start, "ms to open!");
	}

	@Override
	public void draw(boolean focused) {
		int sw = 2* Main.window.width();
		int sh = 2* Main.window.height();

		int wp = sw / 7;
		int bw = Math.min((int) (wp * 1.45f), 600);

		// if there are some empty spots
		// this will make them stick out a little less
		drawBackground(Colors.CARD_BACKGROUND);

		for (DecorCard card : cards) {
			drawBackground(Colors.CARD_SEPARATOR);
			card.draw();
		}

		ScreenRenderer.setColor(Colors.NONE);
		ScreenRenderer.centerAt(-1, 1);
		ScreenRenderer.setOffset(wp, 0);

		for (int i = 0; i < sh / bw + 2; i ++) {
			drawBarSlice(bw, i == 1);
		}

		ScreenRenderer.centerAt(-1, 0);
		ScreenRenderer.setOffset(wp + bw / 2 - 40 * 3 - 1, 100);
		if (ScreenRenderer.button("START", 4, 38, 80, true)) {
			this.close();
			ScreenStack.open(new PlayScreen(WorldHolder.world));
		}

//		ScreenRenderer.offset(0, -100);
//		ScreenRenderer.button("OPTIONS", 4, 38, 80, true);

		ScreenRenderer.offset(0, -100);
		if (ScreenRenderer.button("QUIT", 4, 38, 80, true) ){
			GLFW.glfwSetWindowShouldClose(Main.window.handle, true);
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

	void fillVisible(int w, int h, float scale, int spacing) {
		for (int x = 0; x <= w; x += spacing) {
			for (int y = 0; y <= h; y += spacing) {
				boolean contained = false;

				for (DecorCard card : cards) {
					if (card.has(x, y)) {
						contained = true;
						break;
					}
				}

				if (!contained) {
					cards.add(new DecorCard(source, (int) (150 * 3 * scale), (int) (210 * 3 * scale), x, y));
				}
			}
		}
	}

	@Override
	public void onKey(int key, int action, int mods) {
		super.onKey(key, action, mods);

		if (key == GLFW.GLFW_KEY_R && action == GLFW.GLFW_PRESS) {
			init();
		}
	}

}
