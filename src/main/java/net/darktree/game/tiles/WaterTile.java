package net.darktree.game.tiles;

import net.darktree.core.event.ClickEvent;
import net.darktree.core.world.tile.Tile;
import net.darktree.core.world.World;

public class WaterTile extends Tile {

	@Override
	public void onInteract(World world, int x, int y, ClickEvent event) {
		if (event.isPressed()) {
			world.setTileState(x, y, Tiles.EMPTY.getDefaultVariant());
		}
	}

}
