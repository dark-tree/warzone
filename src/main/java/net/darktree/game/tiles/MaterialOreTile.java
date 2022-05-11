package net.darktree.game.tiles;

import net.darktree.event.ClickEvent;
import net.darktree.lt2d.world.Tile;
import net.darktree.lt2d.world.World;

public class MaterialOreTile extends Tile {

	@Override
	public void onInteract(World world, int x, int y, ClickEvent event) {
		if (event.isPressed()) {
			world.setTileState(x, y, Tiles.MATERIAL_MINE.getDefaultVariant());
		}
	}

	@Override
	public boolean canPathfindOnto(World world, int x, int y) {
		return false;
	}
}
