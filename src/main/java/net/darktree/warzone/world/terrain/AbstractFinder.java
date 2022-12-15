package net.darktree.warzone.world.terrain;

import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.world.World;
import net.darktree.warzone.world.entity.Entity;
import net.darktree.warzone.world.tile.Tile;
import net.darktree.warzone.world.tile.TileState;

import java.util.Arrays;

public abstract class AbstractFinder {

	protected final static int[][] STAR = {
			{-1, +0}, {+0, -1}, {+0, +1}, {+1, +0}
	};

	protected final static int[][] RING = {
			{-1, +0}, {+0, -1}, {-1, -1}, {+1, +0},
			{+0, +1}, {+1, +1}, {+1, -1}, {-1, +1}
	};

	protected final int[][] offsets;
	protected final int width, height;
	protected final World world;
	protected final int[][] field;

	protected AbstractFinder(int[][] pattern, World world) {
		this.offsets = pattern;
		this.world = world;
		this.width = world.width;
		this.height = world.height;
		this.field = new int[this.width][this.height];
	}

	protected final void iterate(WorldIterator iterator) {
		int level = 1;

		for (boolean dirty = true; dirty; level ++) {
			dirty = false;

			for (int x = 0; x < width; x ++) {
				for (int y = 0; y < height; y ++) {
					if (field[x][y] == level) {
						dirty = true;
						iterator.accept(x, y, level + 1);
					}
				}
			}
		}
	}

	protected void clearField(int[][] array2d) {
		for (int[] row : array2d) Arrays.fill(row, 0);
	}

	protected final boolean isPosValid(int x, int y) {
		return x >= 0 && y >= 0 && x < width && y < height;
	}

	protected final TileState getState(int x, int y) {
		return world.getTileState(x, y);
	}

	protected final Symbol getOwner(int x, int y) {
		return getState(x, y).getOwner();
	}

	protected final Tile getTile(int x, int y) {
		return getState(x, y).getTile();
	}

	protected final Entity getEntity(int x, int y) {
		return world.getEntity(x, y);
	}

	protected interface WorldIterator {
		void accept(int x, int y, int level);
	}

}
