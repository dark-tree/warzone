package net.darktree.game.tiles;

import net.darktree.event.ClickEvent;
import net.darktree.lt2d.world.Tile;
import net.darktree.lt2d.world.World;

public class MaterialMineTile extends Tile {

	public boolean canPathfindThrough(World world, int x, int y) {
		return false;
	}

	@Override
	public void onInteract(World world, int x, int y, ClickEvent event) {
		if (event.isPressed()) {
			world.setTileState(x, y, Tiles.MATERIAL.getDefaultVariant());
		}
	}

}
