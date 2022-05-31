package net.darktree.game.tiles;

import net.darktree.core.world.tile.Tile;

public class EmptyTile extends Tile {

	@Override
	public boolean isReplaceable() {
		return true;
	}

}
