package net.darktree.core.world.pattern;

import net.darktree.core.world.tile.TilePos;

import java.util.function.Consumer;

public interface PlacedTileIterator {

	void iterate(Consumer<TilePos> consumer);

}
