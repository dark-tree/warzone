package net.darktree.core.world.terrain;

import net.darktree.core.world.World;
import net.darktree.game.buildings.Building;
import net.darktree.game.country.Symbol;

public class ControlFinder {

	private final int[][] field;

	private final int width, height;
	private final World world;
	private final Symbol symbol;

	private final static int[][] OFFSETS = {
			{-1, +0}, {+0, -1}, {+0, +1}, {+1, +0}
	};

	public ControlFinder(World world, Symbol symbol) {
		this.world = world;
		this.width = world.width;
		this.height = world.height;
		this.symbol = symbol;
		this.field = new int[this.width][this.height];

		if (symbol != Symbol.NONE) {
			Building capitol = world.getCountry(symbol).getCapitol();

			capitol.getPattern().iterate(world, capitol.x, capitol.y, pos -> {
				field[pos.x][pos.y] = 1;
			});
		}else{
			int mx = width - 1;
			int my = height - 1;

			for (int x = 0; x < width; x ++) {
				if (world.getTileState(x, my).getOwner() == symbol) field[x][my] = 1;
				if (world.getTileState(x, 0).getOwner() == symbol) field[x][0] = 1;
			}

			for (int y = 0; y < height; y ++) {
				if (world.getTileState(mx, y).getOwner() == symbol) field[mx][y] = 1;
				if (world.getTileState(0, y).getOwner() == symbol) field[0][y] = 1;
			}
		}

		compute();
	}

	/**
	 * Check if the country controls this point (if it's connected to the capitol)
	 * When checking control for the NONE symbol, it checks for any connection to the world border
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
						propagate(x, y, level + 1);
					}
				}
			}
		}
	}

	private void propagate(int x, int y, int value) {
		for (int[] pair : OFFSETS) {
			set(x + pair[0], y + pair[1], value);
		}
	}

	private void set(int x, int y, int value) {
		if (x >= 0 && y >= 0 && x < width && y < height) {
			Symbol owner = world.getTileState(x, y).getOwner();

			if (owner == this.symbol && this.field[x][y] == 0) {
				this.field[x][y] = value;
			}
		}
	}

}
