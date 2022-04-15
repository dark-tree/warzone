package net.darktree.game.tiles;

import net.darktree.lt2d.world.Tile;
import net.darktree.lt2d.world.state.TileState;
import net.darktree.lt2d.world.state.property.BooleanProperty;

public class CrossTile extends Tile {

	public static BooleanProperty DELETED = EmptyTile.DELETED;

	@Override
	protected TileState createDefaultState() {
		return TileState.createOf(this, DELETED).with(DELETED, false);
	}



}
