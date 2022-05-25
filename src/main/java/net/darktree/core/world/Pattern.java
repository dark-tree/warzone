package net.darktree.core.world;

import net.darktree.core.util.RandomHelper;
import net.darktree.core.world.tile.TilePos;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * This class represents a tile shape in the world, and can be used to query the world
 * for a list of tiles falling withing it.
 */
public class Pattern {

	public static final Pattern EMPTY = new Pattern(new int[][]{
			// Nothing
	});

	public static final Pattern IDENTITY = new Pattern(new int[][]{
			{0, 0}
	});

	public static final Pattern SQUARE = new Pattern(new int[][]{
			{0, 0}, {1, 0}, {0, 1}, {1, 1}
	});

	public static final Pattern SMALL_CROSS = new Pattern(new int[][]{
			{-1, 0}, {0, 0}, {1, 0}, {0, 1}, {0, -1}
	});

	public static final Pattern LARGE_CROSS = new Pattern(new int[][]{
			{-1, 0}, {0, 0}, {1, 0}, {0, 1}, {0, -1}, {-2, 0}, {2, 0}, {0, 2}, {0, -2}, {1, 1}, {-1, -1}, {1, -1}, {-1, 1}
	});

	public final int[][] offsets;

	public Pattern(int[][] offsets) {
		this.offsets = offsets;
	}

	public boolean iterate(World world, int ox, int oy, Consumer<TilePos> consumer) {
		boolean perfect = true;

		for (int[] pos : this.offsets) {
			int x = pos[0] + ox;
			int y = pos[1] + oy;

			if (world.isPositionValid(x, y)) {
				consumer.accept(new TilePos(x, y));
			}else perfect = false;
		}

		return perfect;
	}

	public List<TilePos> list(World world, int ox, int oy, boolean required) {
		List<TilePos> tiles = new ArrayList<>();

		if (required && !iterate(world, ox, oy, tiles::add)) {
			throw new RuntimeException("The pattern did not match perfectly!");
		}

		return tiles;
	}

	public static Pattern nextColonizationPattern() {
		return RandomHelper.nextCubeDigit() == 2 ? Pattern.LARGE_CROSS : Pattern.SMALL_CROSS;
	}

}
