package net.darktree.game.gui;

import net.darktree.Main;
import net.darktree.core.Registries;
import net.darktree.core.client.render.ScreenRenderer;
import net.darktree.core.client.render.image.Sprite;
import net.darktree.game.country.Symbol;

public class PlayUserInterface {

	public static void draw() {
		int i = 4;

		Symbol symbol = Main.world.getCurrentSymbol();

		ScreenRenderer.centerAt(0, 1);
		ScreenRenderer.setTexture(Main.texture, Sprite.IDENTITY);
		ScreenRenderer.box(85 * i, 85 * i, 0, 30 * i);

		if (symbol != null) {
			ScreenRenderer.setTexture(Registries.ATLAS.getTexture(), symbol.getSprite());
			ScreenRenderer.setOffset(-79 * i, -23 * i);
			ScreenRenderer.box(20 * i, 20 * i);
		}

	}

}
