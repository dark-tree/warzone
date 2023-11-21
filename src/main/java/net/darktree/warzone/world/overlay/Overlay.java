package net.darktree.warzone.world.overlay;

import net.darktree.warzone.client.render.color.Color;
import net.darktree.warzone.world.WorldSnapshot;
import net.darktree.warzone.world.tile.TileState;

public abstract class Overlay {

	public abstract Color getColor(WorldSnapshot world, int x, int y, TileState state);

	public void markDirty() {
		// be default do nothing
	}

}
