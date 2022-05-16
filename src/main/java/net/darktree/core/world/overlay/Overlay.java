package net.darktree.core.world.overlay;

import net.darktree.core.util.Color;
import net.darktree.core.world.World;
import net.darktree.core.world.tile.TileState;

public interface Overlay {
	void getColor(World world, int x, int y, TileState state, Color color);
}