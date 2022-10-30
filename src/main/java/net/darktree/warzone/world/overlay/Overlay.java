package net.darktree.warzone.world.overlay;

import net.darktree.warzone.client.render.color.Color;
import net.darktree.warzone.world.World;
import net.darktree.warzone.world.tile.TileState;

public interface Overlay {
	Color getColor(World world, int x, int y, TileState state);
}
