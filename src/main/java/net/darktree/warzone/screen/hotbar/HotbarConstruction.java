package net.darktree.warzone.screen.hotbar;

import net.darktree.warzone.client.Colors;
import net.darktree.warzone.client.Sounds;
import net.darktree.warzone.client.Sprites;
import net.darktree.warzone.client.render.Alignment;
import net.darktree.warzone.client.render.ScreenRenderer;
import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.screen.BuildScreen;
import net.darktree.warzone.screen.PlayScreen;
import net.darktree.warzone.screen.ScreenStack;
import net.darktree.warzone.screen.interactor.DeconstructInteractor;
import net.darktree.warzone.world.World;

public class HotbarConstruction extends HotbarComponent {

	@Override
	public String getNameKey() {
		return "hotbar.construction";
	}

	@Override
	public void draw(boolean focused, World world, Symbol symbol) {
		drawPicker();

		ScreenRenderer.offset(417, 52);
		if (ScreenRenderer.button(Sprites.BUTTON_BUILDING, 66, 66, true) && world.isActiveSymbol()) {
			ScreenStack.open(new BuildScreen(world));
			Sounds.PEN_CLICK.play();
		}

		ScreenRenderer.offset(96, 0);
		if (ScreenRenderer.button(Sprites.BUTTON_DEMOLISH, 66, 66, true) && world.isActiveSymbol()) {
			PlayScreen.setInteractor(new DeconstructInteractor(world.getCurrentSymbol(), world));
			Sounds.PEN_CLICK.play();
		}
	}

	private void drawPicker() {
		ScreenRenderer.push();

		ScreenRenderer.offset(68, 56);
		ScreenRenderer.setAlignment(Alignment.RIGHT);
		ScreenRenderer.literalText(50, "<");

		ScreenRenderer.button(Sprites.FRAME, 58, 58, false);
		ScreenRenderer.setColor(Colors.NONE);
		ScreenRenderer.setSprite(Sprites.ICON_WALL_1);
		ScreenRenderer.box(58, 58);

		ScreenRenderer.offset(92, -4);
		ScreenRenderer.button(Sprites.FRAME, 66, 66, false);
		ScreenRenderer.setColor(Colors.NONE);
		ScreenRenderer.setSprite(Sprites.ICON_WALL_2);
		ScreenRenderer.box(66, 66);

		ScreenRenderer.offset(100, 4);
		ScreenRenderer.button(Sprites.FRAME, 58, 58, false);
		ScreenRenderer.setColor(Colors.NONE);
		//ScreenRenderer.setSprite(Sprites.ICON_WALL_1);
		//ScreenRenderer.box(58, 58);

		ScreenRenderer.setAlignment(Alignment.LEFT);
		ScreenRenderer.offset(50, 0);
		ScreenRenderer.literalText(50, ">");

		ScreenRenderer.pop();
	}

}
