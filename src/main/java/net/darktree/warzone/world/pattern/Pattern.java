package net.darktree.warzone.world.pattern;

import net.darktree.warzone.world.WorldSnapshot;
import net.darktree.warzone.world.tile.TilePos;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

/**
 * This class represents a tile shape in the world, and can be used to query the world
 * for a list of tiles falling withing it.
 */
public abstract class Pattern {

	public static PatternBuilder create() {
		return new PatternBuilder();
	}

	public static Pattern build(int width, int height) {
		return new AreaPattern(width, height);
	}

	public abstract TilePos[] getOffsets();

	public List<TilePos> list(WorldSnapshot world, int ox, int oy, boolean required) {
		List<TilePos> tiles = new ArrayList<>();

		if (!iterate(world, ox, oy, tiles::add) && required) {
			throw new RuntimeException("The pattern did not match perfectly!");
		}

		return tiles;
	}

	public boolean iterate(WorldSnapshot world, int ox, int oy, Consumer<TilePos> consumer) {
		AtomicBoolean perfect = new AtomicBoolean(true);

		for(TilePos pos : getOffsets()) {
			int x = pos.x + ox;
			int y = pos.y + oy;

			if (world.isPositionValid(x, y)) {
				consumer.accept(new TilePos(x, y));
			} else perfect.set(false);
		}

		return perfect.get();
	}

	public PlacedPattern place(WorldSnapshot world, int x, int y) {
		return new PlacedPattern(world, x, y, this);
	}

}
