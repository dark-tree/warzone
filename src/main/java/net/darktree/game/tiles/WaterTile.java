package net.darktree.game.tiles;

import net.darktree.core.event.ClickEvent;
import net.darktree.core.util.Direction;
import net.darktree.core.world.World;
import net.darktree.core.world.tile.Tile;

public class WaterTile extends Tile {

	@Override
	public void onInteract(World world, int x, int y, ClickEvent event) {
		if (event.isPressed()) {
			world.setTileVariant(x, y, Tiles.EMPTY.getDefaultVariant());
		}
	}

	@Override
	public boolean canPenetrate(World world, int x, int y, Direction vector) {
		return true;
	}

}
