package net.darktree.warzone.screen.menu;

import net.darktree.warzone.Main;
import net.darktree.warzone.client.Colors;
import net.darktree.warzone.client.render.ColorMode;
import net.darktree.warzone.client.render.Screen;
import net.darktree.warzone.client.render.ScreenRenderer;
import net.darktree.warzone.util.Logger;
import net.darktree.warzone.util.math.Parallax;

import java.util.ArrayList;
import java.util.List;

public class DecoratedScreen extends Screen {

	private static final Parallax parallax = new Parallax(Main.window, 0.1f);
	private static final List<DecorCard> cards = new ArrayList<>();
	private static final CardSource source = new CardSource(8);
	private static int margin = 0;
	private static boolean ready = false;

	public DecoratedScreen() {
		if (!ready) {
			reloadBackground();
			ready = true;
		}
	}

	@Override
	public void draw(boolean focused) {
		parallax.update();
		int px = (int) (parallax.getX() * margin);
		int py = (int) (parallax.getY() * margin);

		// if there are some empty spots
		// this will make them stick out a little less
		drawBackground(Colors.CARD_BACKGROUND);
		ScreenRenderer.setColorMode(ColorMode.MIXED);

		for (DecorCard card : cards) {
			drawBackground(Colors.CARD_SEPARATOR);
			card.draw(px, py);
		}

		ScreenRenderer.setColorMode(ColorMode.TINT);
	}

	@Override
	public void onResize(int w, int h) {
		reloadBackground();
	}

	public static void reloadBackground() {
		float scale = Main.window.scale();
		long start = System.currentTimeMillis();
		cards.clear();

		int w = Main.window.width();
		int h = Main.window.height();

		margin = Math.max(20, Math.min(w, h) / 20);
		fillVisible(2 * w, 2 * h, scale, 1, margin);
		Logger.info("Background took ", System.currentTimeMillis() - start, "ms to prepare a surface of size ", w, "x", h, " with ", margin, "px of margin.");
	}

	private static void fillVisible(int w, int h, float scale, int spacing, int margin) {
		int wm = w + margin;
		int hm = h + margin;

		for (int x = -margin; x <= wm; x += spacing) {
			for (int y = -margin; y <= hm; y += spacing) {
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
	public void onEscape() {
		// don't close
	}

}
