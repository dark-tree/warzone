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

		ScreenRenderer.from(0, 1).sprite(Main.texture, Sprite.IDENTITY).box(85 * i, 85 * i, 0, 30 * i).endQuads();
		if (symbol != null) ScreenRenderer.from(0, 1).sprite(Registries.ATLAS.getTexture(), symbol.getSprite()).offset(-79 * i, -23 * i).box(20 * i, 20 * i).endQuads();

//		ScreenRenderer.fromMouse().box(10, 10, 10, 10).sprite(Main.texture, sprite).next();
//		ScreenRenderer.fromTile(4, 4).box(30, 30).sprite(Main.texture, sprite).next();


	}

}
