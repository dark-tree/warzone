package net.darktree.warzone.world.terrain;

import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.world.WorldSnapshot;
import net.darktree.warzone.world.pattern.Patterns;
import net.darktree.warzone.world.tile.Surface;

public class ChokepointFinder extends AbstractFieldFinder {

	int[][] vertical, horizontal;

	public ChokepointFinder(WorldSnapshot world) {
		super(Patterns.EMPTY, world);
		this.vertical = new int[width][height];
		this.horizontal = new int[width][height];

		compute();
	}

	public int getTightness(int x, int y) {
		return field[x][y];
	}

	private void compute() {
		for (int x = 0; x < width; x ++) {
			for (int y = 0; y < height; y ++) {
				Symbol owner = getOwner(x, y);
				horizontal[x][y] = getDoubleSpacing(x, y, 1, 0, owner);
				vertical[x][y] = getDoubleSpacing(x, y, 0, 1, owner);
			}
		}

		for (int x = 0; x < width; x ++) {
			for (int y = 0; y < height; y ++) {
				field[x][y] = getMinimal(x, y);
			}
		}
	}

	private int getMinimal(int x, int y) {
		Symbol owner = getOwner(x, y);

		if (getTile(x, y).getSurface() != Surface.LAND) {
			return 0;
		}

		int vs = vertical[x][y];
		int vl = queryMinimalSpacingAlong(x, y, 1, 0, owner, vs, vertical);

		if (vl >= vs) {
			return vs;
		}

		int hs = horizontal[x][y];
		int hl = queryMinimalSpacingAlong(x, y, 0, 1, owner, hs, horizontal);

		if (hl >= hs) {
			return vs;
		}

		return 0;
	}

	private int queryMinimalSpacingAlong(int x, int y, int vx, int vy, Symbol self, int spacing, int[][] source) {
		return Math.min(querySingleSpacingAlong(x, y, vx, vy, self, spacing, source), querySingleSpacingAlong(x, y, -vx, -vy, self, spacing, source));
	}

	private int querySingleSpacingAlong(int x, int y, int vx, int vy, Symbol self, int spacing, int[][] source) {
		int length = 0;

		while (true) {
			x += vx;
			y += vy;

			if (isTileValid(x, y, self)) {
				int here = source[x][y];

				if (here >= spacing) {
					spacing = here;
					length ++;
				}
				continue;
			}

			return length;
		}
	}

	private int getDoubleSpacing(int x, int y, int vx, int vy, Symbol self) {
		return getSingleSpacing(x, y, vx, vy, self) + getSingleSpacing(x, y, -vx, -vy, self) + 1;
	}

	private int getSingleSpacing(int x, int y, int vx, int vy, Symbol self) {
		int length = 0;

		while (true) {
			x += vx;
			y += vy;

			if (!isTileValid(x, y, self)) {
				return length;
			}

			length ++;
		}
	}

	private boolean isTileValid(int x, int y, Symbol self) {
		if (!isPosValid(x, y)) {
			return false;
		}

		final Symbol owner = getOwner(x, y);
		return owner == self && getTile(x, y).getSurface() == Surface.LAND && getTile(x, y).canColonize(self);
	}

}
