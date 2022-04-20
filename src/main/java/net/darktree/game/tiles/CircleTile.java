package net.darktree.game.tiles;

import net.darktree.lt2d.world.Tile;
import net.darktree.lt2d.world.state.TileVariant;
import net.darktree.lt2d.world.state.property.BooleanProperty;

public class CircleTile extends Tile {

	public static BooleanProperty DELETED = EmptyTile.DELETED;

	@Override
	protected TileVariant createDefaultState() {
		return TileVariant.createOf(this, DELETED).with(DELETED, false);
	}

	public boolean canPathfindThrough() {
		return false;
	}

}
