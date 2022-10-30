package net.darktree.warzone.screen.hotbar;

import net.darktree.warzone.client.Colors;
import net.darktree.warzone.client.Sprites;
import net.darktree.warzone.client.render.Alignment;
import net.darktree.warzone.client.render.ScreenRenderer;
import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.world.World;

public class Hotbar {

	private static final int EXTEND = 630;
	private static final int HEIGHT = 170;

	private static final HotbarComponent left = new HotbarOverview();
	private static final HotbarComponent right = new HotbarConstruction();

	public static void draw(boolean focused, World world, Symbol symbol) {

		ScreenRenderer.setSprite(Sprites.HOTBAR);
		ScreenRenderer.centerAt(0, -1);
		ScreenRenderer.box(EXTEND, EXTEND, HEIGHT, 0);

		draw(left, -EXTEND, focused, world, symbol);
		draw(right, 0, focused, world, symbol);

	}

	private static void draw(HotbarComponent component, int x, boolean focused, World world, Symbol symbol) {
		if (component != null) {
			ScreenRenderer.setOffset(x, 0);
			component.draw(focused, world, symbol);
			String name = component.getName();

			ScreenRenderer.setColor(Colors.NONE);

			if (name != null) {
				ScreenRenderer.setOffset(x + EXTEND / 2, HEIGHT - 40);
				ScreenRenderer.setAlignment(Alignment.CENTER);
				ScreenRenderer.text(name, 20);
			}

			ScreenRenderer.setAlignment(Alignment.LEFT);
		}
	}

}
