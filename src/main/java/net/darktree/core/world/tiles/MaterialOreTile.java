package net.darktree.core.world.tiles;

import net.darktree.core.event.ClickEvent;
import net.darktree.core.world.Surface;
import net.darktree.core.world.World;
import net.darktree.core.world.action.ToggleMineAction;
import net.darktree.core.world.tile.Tile;

public class MaterialOreTile extends Tile {

	@Override
	public void onInteract(World world, int x, int y, ClickEvent event) {
		if (event.isPressed()) {
			world.getManager().apply(new ToggleMineAction(x, y));
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
