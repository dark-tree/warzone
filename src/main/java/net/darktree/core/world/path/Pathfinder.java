package net.darktree.core.world.path;

import net.darktree.core.world.Surface;
import net.darktree.core.world.World;
import net.darktree.core.world.entity.Entity;
import net.darktree.core.world.pattern.PlacedTileIterator;
import net.darktree.core.world.tile.Tile;
import net.darktree.core.world.tile.TilePos;
import net.darktree.game.country.Symbol;

public class Pathfinder {

	private final int[][] field;
	private final float[][] distance;

	private final World world;
	private final int width, height;
	private final Symbol symbol;
	private final boolean within;
	private final Surface surface;

	private final static int[][] OFFSETS = {
			{-1, +0}, {+0, -1}, {-1, -1}, {+1, +0},
			{+0, +1}, {+1, +1}, {+1, -1}, {-1, +1}
	};

	// TODO: split max, pattern and within into PathfinderConfig
	public Pathfinder(World world, int max, Symbol symbol, Surface surface, PlacedTileIterator pattern, boolean within) {
		this.world = world;
		this.width = world.width;
		this.height = world.height;
		this.field = new int[this.width][this.height];
		this.distance = new float[this.width][this.height];
		this.symbol = symbol;
		this.within = within;
		this.surface = surface;

		pattern.iterate(pos -> {
			this.field[pos.x][pos.y] = 1;
			this.distance[pos.x][pos.y] = max;
		});

		compute(max);
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

	private void compute(int max) {
		int level = 1;

		for (boolean dirty = true; dirty && level <= max; level ++) {
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

	private void set(int x, int y, int value, float distance) {
		if (x >= 0 && y >= 0 && x < width && y < height) {

			Tile tile = world.getTileState(x, y).getTile();
			Symbol owner = world.getTileState(x, y).getOwner();

			if (!within || owner == this.symbol) {
				float weight = distance - ((this.symbol != owner) ? 2.0f : 1.0f);

				if (this.field[x][y] == 0 && shouldPropagate(x, y, tile)) {
					if (weight > 0) {
						this.field[x][y] = value;
						this.distance[x][y] = weight;
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
