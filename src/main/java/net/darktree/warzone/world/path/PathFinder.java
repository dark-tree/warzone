package net.darktree.warzone.world.path;

import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.world.World;
import net.darktree.warzone.world.entity.Entity;
import net.darktree.warzone.world.pattern.PlacedTileIterator;
import net.darktree.warzone.world.terrain.AbstractFinder;
import net.darktree.warzone.world.tile.Tile;
import net.darktree.warzone.world.tile.TilePos;

public class PathFinder extends AbstractFinder {

	private final int[][] distance;

	private final Symbol symbol;
	private final PathFinderConfig config;
	private final PlacedTileIterator pattern;

	public PathFinder(World world, Symbol symbol, PlacedTileIterator pattern, PathFinderConfig config) {
		super(AbstractFinder.RING, world);

		this.distance = new int[this.width][this.height];
		this.symbol = symbol;
		this.config = config;
		this.pattern = pattern;

		update();
	}

	public void update() {
		clearField(this.field);
		clearField(this.distance);

		pattern.iterate(pos -> {
			this.field[pos.x][pos.y] = 1;
		});

		compute();
	}

	/**
	 * Check if the pathfinder found a path to the given tile
	 */
	public boolean canReach(int x, int y) {
		return this.field[x][y] != 0 && getTile(x, y).canStayOn() && getEntity(x, y) == null;
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
		iterate(this::propagate);
	}

	private boolean shouldPropagate(int x, int y, Tile tile) {
		if (tile.getSurface() != config.surface) {
			return false;
		}

		final Entity entity = getEntity(x, y);
		return entity == null || entity.canPathfindThrough(this.symbol);
	}

	private void propagate(int x, int y, int value) {
		for (int[] pair : offsets) {
			set(x + pair[0], y + pair[1], value, this.distance[x][y]);
		}
	}

	private void set(int x, int y, int value, int distance) {
		if (!isPosValid(x, y)) {
			return;
		}

		final Tile tile = getTile(x, y);
		final Symbol owner = getOwner(x, y);

		if (config.boundary.isValid(owner, this.symbol)) {
			distance += (this.symbol == owner ? 1 : 2);

			if (this.field[x][y] == 0 && shouldPropagate(x, y, tile)) {
				if (distance <= config.steps) {
					this.field[x][y] = value;
					this.distance[x][y] = distance;
				}
			}
		}
	}

	private TilePos getLowerNeighbour(int x, int y) {
		int level = field[x][y] - 1;
		int distance = this.distance[x][y];
		TilePos pos = null;

		for (int[] pair : offsets) {
			final int ox = x + pair[0];
			final int oy = y + pair[1];

			if (!isPosValid(ox, oy)) {
				continue;
			}

			int dist = this.distance[ox][oy];

			if (field[ox][oy] == level && dist < distance) {
				pos = new TilePos(ox, oy);
				distance = dist;
			}
		}

		return pos;
	}

}
