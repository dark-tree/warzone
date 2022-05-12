package net.darktree.game.gui;

import net.darktree.Main;
import net.darktree.game.country.Symbol;
import net.darktree.lt2d.Registries;
import net.darktree.lt2d.graphics.ScreenRenderer;
import net.darktree.lt2d.graphics.image.Sprite;

public class PlayUserInterface {

	// workaround for the broken texture system
	static Sprite sprite = new Sprite(0, 1, 1, 0);

	public static void draw() {
		int i = 4;

		Symbol symbol = Main.world.getCurrentSymbol();

		ScreenRenderer.from(0, 1).sprite(Main.texture, sprite).box(85 * i, 85 * i, 0, 30 * i).endQuads();
		if (symbol != null) ScreenRenderer.from(0, 1).sprite(Registries.ATLAS.texture, symbol.getSprite()).offset(-79 * i, -23 * i).box(20 * i, 20 * i).endQuads();

//		ScreenRenderer.fromMouse().box(10, 10, 10, 10).sprite(Main.texture, sprite).next();
//		ScreenRenderer.fromTile(4, 4).box(30, 30).sprite(Main.texture, sprite).next();


	}

}
