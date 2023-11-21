package net.darktree.warzone.world.terrain;

import net.darktree.warzone.country.Country;
import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.world.Warp;
import net.darktree.warzone.world.WorldSnapshot;
import net.darktree.warzone.world.entity.building.Building;
import net.darktree.warzone.world.pattern.Patterns;
import net.darktree.warzone.world.tile.TilePos;

public class ControlFinder extends AbstractFieldFinder {

	public ControlFinder(WorldSnapshot world) {
		super(Patterns.NEIGHBOURS, world);

		for (Symbol s : Symbol.values()) {
			init(s);
		}

		compute();
	}

	/**
	 * Check if any country controls this point (if it's connected to the capitol)
	 */
	public boolean canControl(int x, int y) {
		return field[x][y] != 0;
	}

	protected void compute() {
		iterate((x, y, level) -> propagate(x, y, level, getOwner(x, y)));
	}

	private void propagate(int x, int y, int value, Symbol symbol) {
		for (TilePos offset : offsets) {
			set(x, y, x + offset.x, y + offset.y, value, symbol);
		}
	}

	private void set(int fx, int fy, int x, int y, int value, Symbol symbol) {
		if (!isPosValid(x, y)) {
			return;
		}

		// handle warps
		if (getEntity(x, y) instanceof Warp warp && warp.isWarpDirect() && warp.canWarpFrom(fx, fy)) {
			for (TilePos pos : warp.getWarpedTiles()) {
				if (!pos.equals(x, y)) {
					set(fx, fy, pos.x, pos.y, value, symbol);
				}
			}
		}

		if (getOwner(x, y) == symbol && this.field[x][y] == 0) {
			this.field[x][y] = value;
		}
	}

	private void init(Symbol symbol) {
		if (symbol != Symbol.NONE) {
			Country country = world.getCountry(symbol);

			if (country != null) {
				Building capitol = country.getCapitol();

				if (capitol != null) {
					capitol.forEachTile(pos -> field[pos.x][pos.y] = 1);
				}
			}
		}else{
			int mx = width - 1;
			int my = height - 1;

			for (int x = 0; x < width; x ++) {
				write(symbol, x, my);
				write(symbol, x, 0);
			}

			for (int y = 0; y < height; y ++) {
				write(symbol, mx, y);
				write(symbol, 0, y);
			}
		}
	}

	private void write(Symbol symbol, int x, int y) {
		if (getOwner(x, y) == symbol) field[x][y] = 1;
	}

}
