package net.darktree.core.world.overlay;

import net.darktree.core.client.render.color.Color;
import net.darktree.core.world.World;
import net.darktree.core.world.tile.TileState;

public interface Overlay {
	Color getColor(World world, int x, int y, TileState state);
}
