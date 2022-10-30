package net.darktree.core.world.tiles;

import net.darktree.core.world.Surface;
import net.darktree.core.world.tile.Tile;

public class EmptyTile extends Tile {

	@Override
	public Surface getSurface() {
		return Surface.LAND;
	}

}
