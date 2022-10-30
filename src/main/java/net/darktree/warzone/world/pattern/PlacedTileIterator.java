package net.darktree.warzone.world.pattern;

import net.darktree.warzone.world.tile.TilePos;

import java.util.function.Consumer;

public interface PlacedTileIterator {

	void iterate(Consumer<TilePos> consumer);

}
