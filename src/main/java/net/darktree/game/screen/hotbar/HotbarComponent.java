package net.darktree.game.screen.hotbar;

import net.darktree.Main;
import net.darktree.core.client.Colors;
import net.darktree.core.client.render.ScreenRenderer;
import net.darktree.core.client.render.image.Sprite;
import net.darktree.core.client.window.input.MouseButton;
import net.darktree.core.world.World;
import net.darktree.game.country.Symbol;

public abstract class HotbarComponent {

	protected String getName() {
		return null;
	}

	protected abstract void draw(boolean focused, World world, Symbol symbol);

	protected boolean button(Sprite sprite, int w, int h) {
		ScreenRenderer.setSprite(sprite);
		boolean hover = ScreenRenderer.isMouseOver(w, h);

		if (hover) {
			ScreenRenderer.setColor(Colors.BUTTON_HOVER);

			if(Main.window.input().isButtonPressed(MouseButton.LEFT)) {
				ScreenRenderer.setColor(Colors.BUTTON_PRESSED);
			}
		}else{
			ScreenRenderer.setColor(Colors.BUTTON_DEFAULT);
		}

		ScreenRenderer.box(66, 66);
		return hover && Main.window.input().hasClicked();
	}

}
