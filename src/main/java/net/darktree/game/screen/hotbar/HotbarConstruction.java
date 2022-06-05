package net.darktree.game.screen.hotbar;

import net.darktree.Main;
import net.darktree.core.client.Colors;
import net.darktree.core.client.Sprites;
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
		// TODO: rewrite

//		ScreenRenderer.offset(385 + 74 + 4, -42 + 4);
//		ScreenRenderer.setAlignment(Alignment.RIGHT);
//		ScreenRenderer.text("<", 50);
//
//		if (ScreenRenderer.isMouseOver(58, 58)) {
//			ScreenRenderer.setColor(Colors.BUTTON_HOVER);
//		}else{
//			ScreenRenderer.setColor(Colors.OVERLAY_NONE);
//		}
//		ScreenRenderer.setSprite(Sprites.FRAME);
//		ScreenRenderer.box(58, 58);
//		ScreenRenderer.setSprite(Sprites.ICON_WALL_1);
//		ScreenRenderer.box(58, 58);
//
//		ScreenRenderer.offset(96 - 4, -4);
//		if (ScreenRenderer.isMouseOver(66, 66)) {
//			ScreenRenderer.setColor(Colors.BUTTON_HOVER);
//		}else{
//			ScreenRenderer.setColor(Colors.OVERLAY_NONE);
//		}
//		ScreenRenderer.setSprite(Sprites.FRAME);
//		ScreenRenderer.box(66, 66);
//		ScreenRenderer.setSprite(Sprites.ICON_WALL_2);
//		ScreenRenderer.box(66, 66);
//
//		ScreenRenderer.offset(96 + 4, 4);
//		if (ScreenRenderer.isMouseOver(58, 58)) {
//			ScreenRenderer.setColor(Colors.BUTTON_HOVER);
//		}else{
//			ScreenRenderer.setColor(Colors.OVERLAY_NONE);
//		}
//		ScreenRenderer.setSprite(Sprites.FRAME);
//		ScreenRenderer.box(58, 58);
//
//		ScreenRenderer.setAlignment(Alignment.LEFT);
//		ScreenRenderer.offset(50, -4);
//		ScreenRenderer.text(">", 50);
//		ScreenRenderer.offset(-385 - 74 - 96 -96 -50 - 4, 42);

		ScreenRenderer.offset(417, 52);
		ScreenRenderer.setSprite(Sprites.BUTTON_BUILDING);
		ScreenRenderer.setColor(Colors.OVERLAY_NONE);
		if (ScreenRenderer.isMouseOver(66, 66)) {
			ScreenRenderer.setColor(Colors.BUTTON_HOVER);

			if(Main.window.input().hasClicked()) {
				ScreenStack.open(new BuildScreen(world));
			}
		}
		ScreenRenderer.box(66, 66);
		ScreenRenderer.offset(96, 0);

		ScreenRenderer.setSprite(Sprites.BUTTON_DEMOLISH);
		ScreenRenderer.setColor(Colors.OVERLAY_NONE);
		if (ScreenRenderer.isMouseOver(66, 66)) {
			ScreenRenderer.setColor(Colors.BUTTON_HOVER);
		}
		ScreenRenderer.box(66, 66);
	}

}
