package net.darktree.warzone.screen.menu;

import net.darktree.warzone.Main;
import net.darktree.warzone.client.Colors;
import net.darktree.warzone.client.render.Screen;
import net.darktree.warzone.util.Logger;
import net.darktree.warzone.util.math.Parallax;

import java.util.ArrayList;
import java.util.List;

public abstract class DecoratedScreen extends Screen {

	private static final Parallax parallax = new Parallax(Main.window, 0.1f);
	private static final List<DecorCard> cards = new ArrayList<>();
	private static final CardSource source = new CardSource(8);
	private static final int margin = 20;
	private static boolean ready = false;

	public DecoratedScreen() {
		if (!ready) {
			reloadBackground();
			ready = true;
		}
	}

	public void drawDecorBackground() {
		parallax.update();
		int px = (int) (parallax.getX() * margin);
		int py = (int) (parallax.getY() * margin);

		// if there are some empty spots
		// this will make them stick out a little less
		drawBackground(Colors.CARD_BACKGROUND);

		for (DecorCard card : cards) {
			drawBackground(Colors.CARD_SEPARATOR);
			card.draw(px, py);
		}
	}

	public void onResize(int w, int h) {
		reloadBackground();
	}

	protected void reloadBackground() {
		float scale = Main.window.scale();
		long start = System.currentTimeMillis();
		cards.clear();
		fillVisible(2 * Main.window.width(), 2 * Main.window.height(), scale, 1, margin);
		Logger.info("Background took ", System.currentTimeMillis() - start, "ms to prepare!");
	}

	private void fillVisible(int w, int h, float scale, int spacing, int margin) {
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

}
