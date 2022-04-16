package net.darktree.lt2d.world.path;

import net.darktree.lt2d.world.TilePos;
import net.darktree.lt2d.world.World;

import java.util.Objects;

// TODO slightly shit make less bad
public class Pathfinder {

	private final int[][] field;
	private final World world;
	private final int width, height;

	public Pathfinder(World world, int x, int y, int max) {
		this.world = world;
		this.width = world.width;
		this.height = world.height;
		this.field = new int[this.width][this.height];
		this.field[x][y] = 1;

		compute(max);
	}

	public boolean canReach(int x, int y) {
		return this.field[x][y] != 0;
	}

	public Path getPathTo(int x, int y) {
		Path path = new Path();
		TilePos pos = new TilePos(x, y);

		do {
			pos = Objects.requireNonNull(getLowerNeighbour(pos.x, pos.y));
			path.addTarget(pos);
		} while (this.field[pos.x][pos.y] != 1);

		path.reverseAll();
		return path;
	}

	private void compute(int max) {
		boolean propagated;
		int level = 1;

		do {
			propagated = false;

			for (int x = 0; x < this.width; x ++) {
				for (int y = 0; y < this.height; y ++) {
					if (field[x][y] == level) {
						propagated = true;
						propagate(x, y, level + 1);
					}
				}
			}

			level++;
		} while (propagated && level <= max);
	}

	private boolean shouldPropagate(int x, int y) {
		return world.getTileState(x, y).getTile().canPathfindThrough();
	}

	private void propagate(int x, int y, int value) {
		set(x-1, y, value);
		set(x, y-1, value);
		set(x-1, y-1, value);
		set(x+1, y, value);
		set(x, y+1, value);
		set(x+1, y+1, value);
		set(x+1, y-1, value);
		set(x-1, y+1, value);
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

		if (get(x-1, y) == level) return new TilePos(x-1, y);
		if (get(x, y-1) == level) return new TilePos(x, y-1);
		if (get(x-1, y-1) == level) return new TilePos(x-1, y-1);
		if (get(x+1, y) == level) return new TilePos(x+1, y);
		if (get(x, y+1) == level) return new TilePos(x, y+1);
		if (get(x+1, y+1) == level) return new TilePos(x+1, y+1);
		if (get(x+1, y-1) == level) return new TilePos(x+1, y-1);
		if (get(x-1, y+1) == level) return new TilePos(x-1, y+1);
		return null;
	}

}
