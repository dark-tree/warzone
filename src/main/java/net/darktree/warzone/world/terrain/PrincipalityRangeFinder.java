package net.darktree.warzone.world.terrain;

import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.world.World;
import net.darktree.warzone.world.pattern.Patterns;
import net.darktree.warzone.world.tile.TilePos;

public class PrincipalityRangeFinder extends AbstractFieldFinder {

	public PrincipalityRangeFinder(World world) {
		super(Patterns.NEIGHBOURS, world);

		compute();
	}

	public int getPrincipalityRange(int x, int y) {
		return field[x][y];
	}

	public Symbol getPrincipality(int x, int y) {
		return world.getPrincipality(x, y);
	}

	private void compute() {
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (isAtBorder(x, y)) {
					this.field[x][y] = 1;
				}
			}
		}

		iterate(this::propagate);
	}

	private boolean isAtBorder(int x, int y) {
		Symbol self = world.getPrincipality(x, y);

		for (TilePos offset : offsets) {
			int ox = x + offset.x;
			int oy = y + offset.y;

			if (isPosValid(ox, oy)) {
				Symbol principality = world.getPrincipality(ox, oy);

				if (principality != Symbol.NONE && principality != self) {
					return true;
				}
			}
		}

		return false;
	}

	private void propagate(int x, int y, int value) {
		for (TilePos offset : offsets) {
			set(x + offset.x, y + offset.y, value);
		}
	}

	private void set(int x, int y, int value) {
		if (isPosValid(x, y) && field[x][y] == 0) {
			this.field[x][y] = value;
		}
	}

}
