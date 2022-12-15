package net.darktree.warzone.world.tile.tiles;

import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.world.tile.Surface;
import net.darktree.warzone.world.tile.Tile;

public class WaterTile extends Tile {

	@Override
	public Surface getSurface() {
		return Surface.WATER;
	}

	@Override
	public boolean canColonize(Symbol enemy) {
		return false;
	}

}
