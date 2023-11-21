package net.darktree.warzone.world.tile.tiles;

import net.darktree.warzone.client.window.input.ClickEvent;
import net.darktree.warzone.world.WorldAccess;
import net.darktree.warzone.world.action.BuildMineAction;
import net.darktree.warzone.world.tile.Surface;
import net.darktree.warzone.world.tile.Tile;

public class MaterialOreTile extends Tile {

	@Override
	public void onInteract(WorldAccess world, int x, int y, ClickEvent event) {
		if (world.isActiveSymbol()) { // owner is checked in the action
			world.getLedger().push(new BuildMineAction(x, y));
		}
	}

	@Override
	public Surface getSurface() {
		return Surface.LAND;
	}

	@Override
	public boolean canStayOn() {
		return true;
	}

}
