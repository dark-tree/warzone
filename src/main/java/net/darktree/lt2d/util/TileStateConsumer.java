package net.darktree.lt2d.util;

import net.darktree.lt2d.world.TileState;

public interface TileStateConsumer {

	void accept(TileState state, int x, int y);

}
