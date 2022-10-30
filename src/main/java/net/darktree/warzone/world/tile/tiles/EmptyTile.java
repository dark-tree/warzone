package net.darktree.warzone.world.tile.tiles;

import net.darktree.warzone.world.tile.Surface;
import net.darktree.warzone.world.tile.Tile;

public class EmptyTile extends Tile {

	@Override
	public Surface getSurface() {
		return Surface.LAND;
	}

}
