package net.darktree.warzone.world.tile.tiles;

import net.darktree.warzone.event.ClickEvent;
import net.darktree.warzone.world.World;
import net.darktree.warzone.world.action.BuildMineAction;
import net.darktree.warzone.world.tile.Surface;
import net.darktree.warzone.world.tile.Tile;

public class MaterialOreTile extends Tile {

	@Override
	public void onInteract(World world, int x, int y, ClickEvent event) {
		if (event.isPressed()) {
			world.getManager().apply(new BuildMineAction(world, x, y));
		}
	}

	@Override
	public Surface getSurface() {
		return Surface.LAND;
	}

	@Override
	public boolean canStayOn() {
		return false;
	}

}
