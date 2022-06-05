package net.darktree.game.screen.hotbar;

import net.darktree.core.client.Sprites;
import net.darktree.core.client.render.Alignment;
import net.darktree.core.client.render.ScreenRenderer;
import net.darktree.core.world.World;
import net.darktree.game.country.Symbol;

public class Hotbar {

	private static final HotbarComponent left = new HotbarOverview();
	private static final HotbarComponent right = new HotbarConstruction();

	public static void draw(boolean focused, World world, Symbol symbol) {

		ScreenRenderer.setSprite(Sprites.HOTBAR);
		ScreenRenderer.centerAt(0, -1);
		ScreenRenderer.box(630, 630, 170, 0);

		draw(left, -630, focused, world, symbol);
		draw(right, 0, focused, world, symbol);

	}

	private static void draw(HotbarComponent component, int x, boolean focused, World world, Symbol symbol) {
		if (component != null) {
			ScreenRenderer.setOffset(x, 0);
			component.draw(focused, world, symbol);
			String name = component.getName();

			if (name != null) {
				ScreenRenderer.setOffset(x + 315, 130);
				ScreenRenderer.setAlignment(Alignment.CENTER);
				ScreenRenderer.text(name, 20);
			}

			ScreenRenderer.setColor(0, 0, 0, 0);
			ScreenRenderer.setAlignment(Alignment.LEFT);
		}
	}

}
