package net.darktree.game.tiles;

import net.darktree.core.util.Direction;
import net.darktree.core.world.World;
import net.darktree.core.world.tile.Tile;

public class EmptyTile extends Tile {

	@Override
	public boolean isReplaceable() {
		return true;
	}

	@Override
	public boolean canPenetrate(World world, int x, int y, Direction vector) {
		return true;
	}

}
