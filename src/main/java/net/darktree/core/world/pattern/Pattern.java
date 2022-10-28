package net.darktree.core.world.pattern;

import net.darktree.core.world.World;
import net.darktree.core.world.tile.TilePos;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

/**
 * This class represents a tile shape in the world, and can be used to query the world
 * for a list of tiles falling withing it.
 */
public abstract class Pattern {

	protected abstract void forEachTile(Consumer<TilePos> consumer);

	public List<TilePos> list(World world, int ox, int oy, boolean required) {
		List<TilePos> tiles = new ArrayList<>();

		if (!iterate(world, ox, oy, tiles::add) && required) {
			throw new RuntimeException("The pattern did not match perfectly!");
		}

		return tiles;
	}

	public boolean iterate(World world, int ox, int oy, Consumer<TilePos> consumer) {
		AtomicBoolean perfect = new AtomicBoolean(true);

		forEachTile(pos -> {
			int x = pos.x + ox;
			int y = pos.y + oy;

			if (world.isPositionValid(x, y)) {
				consumer.accept(new TilePos(x, y));
			} else perfect.set(false);
		});

		return perfect.get();
	}

	public PlacedPattern place(World world, int x, int y) {
		return new PlacedPattern(world, x, y, this);
	}

}
