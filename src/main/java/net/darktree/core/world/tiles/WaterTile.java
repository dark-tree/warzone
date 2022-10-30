package net.darktree.core.world.tiles;

import net.darktree.core.world.Surface;
import net.darktree.core.world.tile.Tile;

public class WaterTile extends Tile {

	@Override
	public Surface getSurface() {
		return Surface.WATER;
	}

}
