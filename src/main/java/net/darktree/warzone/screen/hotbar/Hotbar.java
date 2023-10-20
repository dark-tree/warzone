package net.darktree.warzone.screen.hotbar;

import net.darktree.warzone.client.Colors;
import net.darktree.warzone.client.render.Alignment;
import net.darktree.warzone.client.render.ScreenRenderer;
import net.darktree.warzone.client.render.image.Sprite;
import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.world.World;

public class Hotbar {

	private static final int EXTEND = 630;
	private static final int HEIGHT = 170;

	private final Sprite sprite;
	private HotbarComponent left = null;
	private HotbarComponent right = null;

	public Hotbar(Sprite sprite) {
		this.sprite = sprite;
	}

	public Hotbar left(HotbarComponent left) {
		this.left = left;
		return this;
	}

	public Hotbar right(HotbarComponent right) {
		this.right = right;
		return this;
	}

	public void draw(boolean focused, World world, Symbol symbol) {
		ScreenRenderer.setSprite(sprite);
		ScreenRenderer.centerAt(0, -1);
		ScreenRenderer.box(EXTEND, EXTEND, HEIGHT, 0);

		if (left != null) draw(left, -EXTEND, focused, world, symbol);
		if (right != null) draw(right, 0, focused, world, symbol);
	}

	private void draw(HotbarComponent component, int x, boolean focused, World world, Symbol symbol) {
		if (component != null) {
			ScreenRenderer.setOffset(x, 0);
			component.draw(focused, world, symbol);
			String name = component.getNameKey();

			ScreenRenderer.setColor(Colors.NONE);

			if (name != null) {
				ScreenRenderer.setOffset(x + EXTEND / 2, HEIGHT - 40);
				ScreenRenderer.setAlignment(Alignment.CENTER);
				ScreenRenderer.translatedText(20, name);
			}

			ScreenRenderer.setAlignment(Alignment.LEFT);
		}
	}

}
