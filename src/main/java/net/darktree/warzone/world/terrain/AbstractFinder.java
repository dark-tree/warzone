package net.darktree.warzone.world.terrain;

import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.world.WorldSnapshot;
import net.darktree.warzone.world.entity.Entity;
import net.darktree.warzone.world.pattern.Pattern;
import net.darktree.warzone.world.pattern.Patterns;
import net.darktree.warzone.world.tile.Surface;
import net.darktree.warzone.world.tile.Tile;
import net.darktree.warzone.world.tile.TilePos;
import net.darktree.warzone.world.tile.TileState;

import java.util.Arrays;

public abstract class AbstractFinder {

	protected final TilePos[] offsets;
	protected final int width, height;
	protected final WorldSnapshot world;

	protected AbstractFinder(Pattern pattern, WorldSnapshot world) {
		this.offsets = pattern.getOffsets();
		this.world = world;
		this.width = world.getInfo().width;
		this.height = world.getInfo().height;
	}

	protected final void clearField(int[][] array2d) {
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

	protected final int getGain(int x, int y) {
		int gain = 0;
		Symbol center = getOwner(x, y);

		for (TilePos offset : Patterns.NEIGHBOURS.getOffsets()) {
			int tx = offset.x + x;
			int ty = offset.y + y;

			if (isPosValid(tx, ty) && getOwner(tx, ty) == Symbol.NONE && getTile(tx, ty).canColonize(center) && getTile(tx, ty).getSurface() == Surface.LAND) {
				gain ++;
			}
		}

		return gain;
	}

}
