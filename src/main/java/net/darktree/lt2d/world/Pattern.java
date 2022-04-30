package net.darktree.lt2d.world;

import net.darktree.lt2d.util.RandomHelper;

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

	public void iterate(World world, int ox, int oy, Consumer<TilePos> consumer) {
		for (int[] pos : this.offsets) {
			int x = pos[0] + ox;
			int y = pos[1] + oy;

			if (world.isPositionValid(x, y)) {
				consumer.accept(new TilePos(x, y));
			}
		}
	}

	public List<TilePos> list(World world, int ox, int oy) {
		List<TilePos> tiles = new ArrayList<>();
		iterate(world, ox, oy, tiles::add);
		return tiles;
	}

	public static Pattern nextColonizationPattern() {
		return RandomHelper.nextCubeDigit() == 2 ? Pattern.LARGE_CROSS : Pattern.SMALL_CROSS;
	}

}
