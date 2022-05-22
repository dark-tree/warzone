package net.darktree.game.gui;

import net.darktree.Main;
import net.darktree.core.Registries;
import net.darktree.core.client.render.ScreenRenderer;
import net.darktree.core.client.render.image.Sprite;
import net.darktree.core.util.Logger;
import net.darktree.game.country.Symbol;

public class PlayUserInterface {

	public static void draw() {
		int i = 4;

		Symbol symbol = Main.world.getCurrentSymbol();

		ScreenRenderer.setTexture(Main.texture, Sprite.IDENTITY);

		ScreenRenderer.centerAt(0, 0);
		if (ScreenRenderer.button(100, 100)) {
			Logger.info("Button pressed!");
		}

		ScreenRenderer.centerAt(0, 1);

		ScreenRenderer.box(60 * i, 60 * i, 0, 30 * i);

		if (symbol != null) {
			ScreenRenderer.setTexture(Registries.ATLAS.getTexture(), symbol.getSprite());
			ScreenRenderer.setOffset(-55 * i, -98);
			ScreenRenderer.box(20 * i, 20 * i);
		}

	}

}
