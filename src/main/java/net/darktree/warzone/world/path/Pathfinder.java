package net.darktree.warzone.world.path;

import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.world.World;
import net.darktree.warzone.world.entity.Entity;
import net.darktree.warzone.world.pattern.PlacedTileIterator;
import net.darktree.warzone.world.tile.Surface;
import net.darktree.warzone.world.tile.Tile;
import net.darktree.warzone.world.tile.TilePos;

import java.util.Arrays;

public class Pathfinder {

	public enum Bound {
		NONE,
		WITHIN,
		COLONIZED
	}

	private final int[][] field;
	private final int[][] distance;

	private final World world;
	private final int width, height;
	private final Symbol symbol;
	private final Bound bound;
	private final Surface surface;
	private final PlacedTileIterator pattern;
	private final int max;

	private final static int[][] OFFSETS = {
			{-1, +0}, {+0, -1}, {-1, -1}, {+1, +0},
			{+0, +1}, {+1, +1}, {+1, -1}, {-1, +1}
	};

	//TODO: split max, pattern and within into PathfinderConfig
	public Pathfinder(World world, int max, Symbol symbol, Surface surface, PlacedTileIterator pattern, Bound bound) {
		this.world = world;
		this.width = world.width;
		this.height = world.height;
		this.field = new int[this.width][this.height];
		this.distance = new int[this.width][this.height];
		this.symbol = symbol;
		this.bound = bound;
		this.surface = surface;
		this.pattern = pattern;
		this.max = max;

		update();
	}

	public void update() {
		for (int[] row : field) Arrays.fill(row, 0);
		for (int[] row : distance) Arrays.fill(row, 0);

		pattern.iterate(pos -> {
			this.field[pos.x][pos.y] = 1;
		});

		compute();
	}

	/**
	 * Check if the pathfinder found a path to the given tile
	 */
	public boolean canReach(int x, int y) {
		return this.field[x][y] != 0 && world.getTileState(x, y).getTile().canStayOn() && world.getEntity(x, y) == null;
	}

	/**
	 * Get path to the given tile
	 */
	public Path getPathTo(int x, int y) {
		Path path = new Path();
		TilePos pos = new TilePos(x, y);
		path.addTarget(pos);

		while (this.field[pos.x][pos.y] != 1) {
			pos = getLowerNeighbour(pos.x, pos.y);
			path.addTarget(pos);
		}

		path.reverseAll();
		return path;
	}

	private void compute() {
		int level = 1;

		for (boolean dirty = true; dirty; level ++) {
			dirty = false;

			for (int x = 0; x < width; x ++) {
				for (int y = 0; y < height; y ++) {
					if (field[x][y] == level) {
						dirty = true;
						propagate(x, y, level + 1);
					}
				}
			}
		}
	}

	private boolean shouldPropagate(int x, int y, Tile tile) {
		if (tile.getSurface() != surface) {
			return false;
		}

		Entity entity = world.getEntity(x, y);
		return entity == null || entity.canPathfindThrough(this.symbol);
	}

	private void propagate(int x, int y, int value) {
		for (int[] pair : OFFSETS) {
			set(x + pair[0], y + pair[1], value, this.distance[x][y]);
		}
	}

	private void set(int x, int y, int value, int distance) {
		if (x >= 0 && y >= 0 && x < width && y < height) {

			Tile tile = world.getTileState(x, y).getTile();
			Symbol owner = world.getTileState(x, y).getOwner();

			if (bound == Bound.NONE || owner == this.symbol || (bound == Bound.COLONIZED && owner != Symbol.NONE)) {
				distance += (this.symbol == owner ? 1 : 2);

				if (this.field[x][y] == 0 && shouldPropagate(x, y, tile)) {
					if (distance <= this.max) {
						this.field[x][y] = value;
						this.distance[x][y] = distance;
					}
				}
			}
		}
	}

	private int get(int x, int y) {
		if (x >= 0 && y >= 0 && x < width && y < height) {
			return this.field[x][y];
		}

		return 0;
	}

	private TilePos getLowerNeighbour(int x, int y) {
		int level = get(x, y) - 1;

		for (int[] pair : OFFSETS) {
			if (get(x + pair[0], y + pair[1]) == level) return new TilePos(x + pair[0], y + pair[1]);
		}

		throw new RuntimeException("Pathfinder encountered an invalid internal state!");
	}

}
