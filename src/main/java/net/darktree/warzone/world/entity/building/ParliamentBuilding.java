package net.darktree.warzone.world.entity.building;

import net.darktree.warzone.client.window.input.ClickEvent;
import net.darktree.warzone.screen.ParliamentScreen;
import net.darktree.warzone.screen.ScreenStack;
import net.darktree.warzone.world.World;
import net.darktree.warzone.world.tile.tiles.Tiles;

public class ParliamentBuilding extends Building {

	public ParliamentBuilding(World world, int x, int y) {
		super(world, x, y, Tiles.PARLIAMENT);
	}

	@Override
	public void onInteract(World world, int x, int y, ClickEvent event) {
		if (event.isPressed()) {
			ScreenStack.open(new ParliamentScreen(world, getOwner().symbol));
		}
	}

}
