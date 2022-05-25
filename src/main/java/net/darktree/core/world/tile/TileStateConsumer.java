package net.darktree.core.world.tile;

public interface TileStateConsumer {

	void accept(TileState state, int x, int y);

}
