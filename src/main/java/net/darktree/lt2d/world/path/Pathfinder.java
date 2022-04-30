package net.darktree.lt2d.world.path;

import net.darktree.lt2d.world.TilePos;
import net.darktree.lt2d.world.World;

// TODO slightly shit make less bad
public class Pathfinder {

	private final int[][] field;
	private final World world;
	private final int width, height;

	private final static int[][] OFFSETS = {
			{-1, +0}, {+0, -1}, {-1, -1}, {+1, +0},
			{+0, +1}, {+1, +1}, {+1, -1}, {-1, +1}
	};

	public Pathfinder(World world, int x, int y, int max) {
		this.world = world;
		this.width = world.width;
		this.height = world.height;
		this.field = new int[this.width][this.height];
		this.field[x][y] = 1;

		compute(max);
	}

	/**
	 * Check if the pathfinder found a path to the given tile
	 */
	public boolean canReach(int x, int y) {
		return this.field[x][y] != 0 && world.getTileState(x, y).getTile().canPathfindOnto(world, x, y) && world.getEntity(x, y) == null;
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

	private boolean shouldPropagate(int x, int y) {
		return world.getTileState(x, y).getTile().canPathfindThrough(world, x, y);
	}

	private void propagate(int x, int y, int value) {
		for (int[] pair : OFFSETS) {
			set(x + pair[0], y + pair[1], value);
		}
	}

	private void set(int x, int y, int value) {
		if (x >= 0 && y >= 0 && x < width && y < height) {
			if (this.field[x][y] == 0 && shouldPropagate(x, y)) {
				this.field[x][y] = value;
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
