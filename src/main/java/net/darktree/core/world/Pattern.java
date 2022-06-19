package net.darktree.core.world;

import net.darktree.core.world.tile.TilePos;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * This class represents a tile shape in the world, and can be used to query the world
 * for a list of tiles falling withing it.
 */
public class Pattern {

	public static final Pattern EMPTY = Pattern.create().build();
	public static final Pattern IDENTITY = Pattern.create().add(0, 0).build();
	public static final Pattern SQUARE = Pattern.create().add(0, 0).add(1, 0).add(0, 1).add(1, 1).build();
	public static final Pattern SMALL_CROSS = Pattern.create().addManhattan(1).build();
	public static final Pattern LARGE_CROSS = Pattern.create().addManhattan(2).build();
	public static final Pattern STAR_SMALL = Pattern.create().add(-1, -1).add(-1, 1).add(1, 1).add(1, -1).build();
	public static final Pattern STAR_LARGE = Pattern.create().add(2, 0).add(-2, 0).add(0, 2).add(0, -2).build();

	public final int[][] offsets;

	Pattern(int[][] offsets) {
		this.offsets = offsets;
	}

	public static PatternBuilder create() {
		return new PatternBuilder();
	}

	public List<TilePos> list(World world, int ox, int oy, boolean required) {
		List<TilePos> tiles = new ArrayList<>();

		if (!iterate(world, ox, oy, tiles::add) && required) {
			throw new RuntimeException("The pattern did not match perfectly!");
		}

		return tiles;
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

	public PlacedPattern place(World world, int x, int y) {
		return new PlacedPattern(world, x, y, this);
	}

	public static Pattern nextColonizationPattern(int dice) {
		return dice == 2 ? Pattern.LARGE_CROSS : Pattern.SMALL_CROSS;
	}

}
