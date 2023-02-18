package net.darktree.warzone.world.terrain;

import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.world.entity.UnitEntity;
import net.darktree.warzone.world.pattern.Patterns;
import net.darktree.warzone.world.tile.TilePos;

public class PrincipalityFinder extends AbstractFieldFinder {

	private final Symbol[][] principality;
	private final BorderFinder finder;

	public PrincipalityFinder(BorderFinder finder) {
		super(Patterns.NEIGHBOURS, finder.world);
		this.principality = new Symbol[width][height];
		this.finder = finder;

		compute();
	}

	public Symbol getPrincipality(int x, int y) {
		return principality[x][y];
	}

	protected void compute() {
		clearField(field);

		for (int x = 0; x < width; x ++) {
			for (int y = 0; y < height; y ++) {
				Symbol owner = getOwner(x, y);

				if (owner != Symbol.NONE) {
					borderCheck(x, y);
					field[x][y] = 1;
				}

				principality[x][y] = owner;
			}
		}

		boolean dirty = true;
		int level = 1;

		while (dirty) {
			dirty = false;

			for (Symbol symbol : Symbol.values()) {
				if (symbol == Symbol.NONE) continue;

				for (int x = 0; x < width; x++) {
					for (int y = 0; y < height; y++) {
						if (field[x][y] == level && principality[x][y] == symbol) {
							dirty |= propagate(x, y, level + 1, symbol);
						}
					}
				}
			}

			level ++;
		}
	}

	private boolean propagate(int x, int y, int value, Symbol turn) {
		boolean dirty = false;

		for (TilePos offset : offsets) {
			dirty |= set(x + offset.x, y + offset.y, value, turn);
		}

		return dirty;
	}

	private boolean set(int x, int y, int level, Symbol turn) {
		if (isPosValid(x, y) && field[x][y] == 0 && getTile(x, y).canColonize(turn)) {
			field[x][y] = level;
			principality[x][y] = turn;
			return true;
		}

		return false;
	}

	private void borderCheck(int x, int y) {
		if (finder.isBorderTile(x, y) && isGuardAt(x, y)) {
			for (TilePos offset : offsets) {
				int tx = x + offset.x, ty = y + offset.y;
				set(tx, ty, 1, getOwner(x, y));
			}
		}
	}

	private boolean isGuardAt(int x, int y) {
		if (getEntity(x, y) instanceof UnitEntity unit) {
			return unit.getSymbol() == getOwner(x, y);
		}

		return false;
	}

}
