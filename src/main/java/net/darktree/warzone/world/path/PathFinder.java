package net.darktree.warzone.world.path;

import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.world.Warp;
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
		return canPossiblyReach(x, y) && distance[x][y] <= config.steps && getTile(x, y).canStayOn() && getEntity(x, y) == null;
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

	private boolean shouldPropagate(Entity entity, Tile tile) {
		if (tile.getSurface() != config.surface) {
			return false;
		}

		return entity == null || entity.canPathfindThrough(this.symbol);
	}

	private void propagate(int x, int y, int value) {
		for (TilePos offset : offsets) {
			set(x, y, x + offset.x, y + offset.y, value, this.distance[x][y]);
		}
	}

	private void set(int ox, int oy, int x, int y, int value, int distance) {
		if (!isPosValid(x, y)) {
			return;
		}

		if (this.field[x][y] == 0) {
			final Tile tile = getTile(x, y);
			final Symbol owner = getOwner(x, y);
			final Entity entity = getEntity(x, y);

			if (entity instanceof Warp warp && warp.canWarpFrom(ox, oy)) {
				for (TilePos pos : warp.getWarpedTiles()) {
					set(ox, oy, pos.x, pos.y, value, distance);
				}
				return;
			}

			if (config.boundary.isValid(owner, this.symbol)) {
				distance += (this.symbol == owner ? 1 : 2);

				if (shouldPropagate(entity, tile)) {
					if (extended || distance <= config.steps) {
						this.field[x][y] = value;
						this.distance[x][y] = distance;
					}
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
				continue;
			}

			if (getEntity(ox, oy) instanceof Warp warp && warp.canWarpFrom(x, y)) {
				for (TilePos exit : warp.getWarpedTiles()) {
					dist = this.distance[exit.x][exit.y];

					if (field[exit.x][exit.y] == level && dist < distance) {
						pos = exit;
						distance = dist;
					}
				}
			}
		}

		// should never happen
		if (pos == null) {
			throw new RuntimeException("Unable to back propagate from (" + x + " " + y + "), is there a faulty warp?");
		}

		return pos;
	}

}
