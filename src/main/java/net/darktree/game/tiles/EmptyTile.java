package net.darktree.game.tiles;

import net.darktree.core.world.World;
import net.darktree.core.world.tile.Tile;

public class EmptyTile extends Tile {

	@Override
	public boolean canPathfindThrough(World world, int x, int y) {
		return true;
	}

}
