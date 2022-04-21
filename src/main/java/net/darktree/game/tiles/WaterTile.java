package net.darktree.game.tiles;

import net.darktree.lt2d.world.Tile;
import net.darktree.lt2d.world.World;
import org.lwjgl.glfw.GLFW;

public class WaterTile extends Tile {

	@Override
	public void onInteract(World world, int x, int y, int mode) {
		if (mode == GLFW.GLFW_PRESS) {
			world.setTileState(x, y, Tiles.EMPTY.getDefaultVariant());
		}
	}

}
