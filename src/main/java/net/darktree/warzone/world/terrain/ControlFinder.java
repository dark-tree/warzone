package net.darktree.warzone.world.terrain;

import net.darktree.warzone.country.Country;
import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.world.World;
import net.darktree.warzone.world.entity.building.Building;

public class ControlFinder extends AbstractFinder {

	public ControlFinder(World world) {
		super(AbstractFinder.STAR, world);

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
		for (int[] pair : offsets) {
			set(x + pair[0], y + pair[1], value, symbol);
		}
	}

	private void set(int x, int y, int value, Symbol symbol) {
		if (!isPosValid(x, y)) {
			return;
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
