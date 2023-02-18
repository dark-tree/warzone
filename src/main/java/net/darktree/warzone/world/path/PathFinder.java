package net.darktree.warzone.world.path;

import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.world.World;
import net.darktree.warzone.world.entity.Entity;
import net.darktree.warzone.world.pattern.PlacedTileIterator;
import net.darktree.warzone.world.terrain.AbstractFieldFinder;
import net.darktree.warzone.world.tile.Tile;
import net.darktree.warzone.world.tile.TilePos;

public class PathFinder extends AbstractFieldFinder {

	private final int[][] distance;

	private final Symbol symbol;
	private final PathFinderConfig config;
	private final PlacedTileIterator pattern;
	private final boolean extended;

	public PathFinder(World world, Symbol symbol, PlacedTileIterator pattern, PathFinderConfig config, boolean extended) {
		super(config.pattern, world);

		this.distance = new int[this.width][this.height];
		this.symbol = symbol;
		this.config = config;
		this.pattern = pattern;
		this.extended = extended;

		update();
	}

	/**
	 * Recompute the world, should be called after the world is updated
	 */
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
		return canPossiblyReach(x, y) && distance[x][y] < config.steps && getTile(x, y).canStayOn() && getEntity(x, y) == null;
	}

	/**
	 * Check if the pathfinder found a path to the given tile, and ignore if the target is actually valid
	 * or within range (if running in extended mode)
	 */
	public boolean canPossiblyReach(int x, int y) {
		return this.field[x][y] != 0;
	}

	/**
	 * Get path to the given tile
	 */
	public Path getPathTo(int x, int y) {
		return getPathProvider(x, y).getFullPath();
	}

	/**
	 * Get path provider for the given target
	 */
	public PathProvider getPathProvider(int x, int y) {
		Path.Recorder recorder = Path.getRecorder();
		TilePos pos = new TilePos(x, y);
		recorder.addTarget(pos);

		while (this.field[pos.x][pos.y] != 1) {
			pos = getLowerNeighbour(pos.x, pos.y);
			recorder.addTarget(pos);
		}

		return new PathProvider(this, recorder.reverse().build());
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
		for (TilePos offset : offsets) {
			set(x + offset.x, y + offset.y, value, this.distance[x][y]);
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
				if (extended || distance <= config.steps) {
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

		for (TilePos offset : offsets) {
			final int ox = x + offset.x;
			final int oy = y + offset.y;

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
