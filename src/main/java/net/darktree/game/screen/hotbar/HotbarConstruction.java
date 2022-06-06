package net.darktree.game.screen.hotbar;

import net.darktree.core.client.Sprites;
import net.darktree.core.client.render.Alignment;
import net.darktree.core.client.render.ScreenRenderer;
import net.darktree.core.world.World;
import net.darktree.game.country.Symbol;
import net.darktree.game.screen.BuildScreen;
import net.darktree.game.screen.ScreenStack;

public class HotbarConstruction extends HotbarComponent {

	@Override
	public String getName() {
		return "CONSTRUCTION";
	}

	@Override
	public void draw(boolean focused, World world, Symbol symbol) {
		drawPicker();

		ScreenRenderer.offset(417, 52);
		if (ScreenRenderer.button(Sprites.BUTTON_BUILDING, 66, 66)) {
			ScreenStack.open(new BuildScreen(world));
		}

		ScreenRenderer.offset(96, 0);
		if (ScreenRenderer.button(Sprites.BUTTON_DEMOLISH, 66, 66)) {
			// TODO: demolish interactor
		}
	}

	private void drawPicker() {
		ScreenRenderer.push();

		ScreenRenderer.offset(68, 56);
		ScreenRenderer.setAlignment(Alignment.RIGHT);
		ScreenRenderer.text("<", 50);

		ScreenRenderer.button(Sprites.FRAME, 58, 58);
		ScreenRenderer.setSprite(Sprites.ICON_WALL_1);
		ScreenRenderer.box(58, 58);

		ScreenRenderer.offset(92, -4);
		ScreenRenderer.button(Sprites.FRAME, 66, 66);
		ScreenRenderer.setSprite(Sprites.ICON_WALL_2);
		ScreenRenderer.box(66, 66);

		ScreenRenderer.offset(100, 4);
		ScreenRenderer.button(Sprites.FRAME, 58, 58);
		//ScreenRenderer.setSprite(Sprites.ICON_WALL_1);
		//ScreenRenderer.box(58, 58);

		ScreenRenderer.setAlignment(Alignment.LEFT);
		ScreenRenderer.offset(50, 0);
		ScreenRenderer.text(">", 50);

		ScreenRenderer.pop();
	}

}
