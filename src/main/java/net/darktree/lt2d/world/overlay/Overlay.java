package net.darktree.lt2d.world.overlay;

import net.darktree.lt2d.world.TileState;
import net.darktree.lt2d.world.World;

public interface Overlay {
	void getColor(World world, int x, int y, TileState state, Color color);
}
