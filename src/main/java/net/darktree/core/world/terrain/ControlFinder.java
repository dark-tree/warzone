package net.darktree.core.world.terrain;

import net.darktree.core.world.World;
import net.darktree.game.buildings.Building;
import net.darktree.game.country.Country;
import net.darktree.game.country.Symbol;
import org.jetbrains.annotations.Nullable;

public class ControlFinder {

	private final int[][] field;

	private final int width, height;
	private final World world;

	private final static int[][] OFFSETS = {
			{-1, +0}, {+0, -1}, {+0, +1}, {+1, +0}
	};

	public ControlFinder(World world, @Nullable Symbol symbol) {
		this.world = world;
		this.width = world.width;
		this.height = world.height;
		this.field = new int[this.width][this.height];

		if (symbol == null) {
			for (Symbol s : Symbol.values()) {
				init(s);
			}
		} else {
			init(symbol);
		}

		compute();
	}

	/**
	 * Check if the country controls this point (if it's connected to the capitol)
	 * When checking control for the NONE symbol, it checks for any connection to the world border
	 * When symbols was set to null it checks if anything controls a tile (including NONE)
	 */
	public boolean canControl(int x, int y) {
		return field[x][y] != 0;
	}

	private void compute() {
		int level = 1;

		for (boolean dirty = true; dirty; level ++) {
			dirty = false;

			for (int x = 0; x < width; x ++) {
				for (int y = 0; y < height; y ++) {
					if (field[x][y] == level) {
						dirty = true;
						propagate(x, y, level + 1, world.getTileState(x, y).getOwner());
					}
				}
			}
		}
	}

	private void propagate(int x, int y, int value, Symbol symbol) {
		for (int[] pair : OFFSETS) {
			set(x + pair[0], y + pair[1], value, symbol);
		}
	}

	private void set(int x, int y, int value, Symbol symbol) {
		if (x >= 0 && y >= 0 && x < width && y < height) {
			Symbol owner = world.getTileState(x, y).getOwner();

			if (owner == symbol && this.field[x][y] == 0) {
				this.field[x][y] = value;
			}
		}
	}

	private void init(Symbol symbol) {
		if (symbol != Symbol.NONE) {
			Country country = world.getCountry(symbol);

			if (country != null) {
				Building capitol = country.getCapitol();

				if (capitol != null) {
					capitol.getPattern().iterate(world, capitol.x, capitol.y, pos -> {
						field[pos.x][pos.y] = 1;
					});
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
		if (world.getTileState(x, y).getOwner() == symbol) {
			field[x][y] = 1;
		}
	}

}
