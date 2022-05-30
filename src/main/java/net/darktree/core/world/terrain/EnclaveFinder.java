package net.darktree.core.world.terrain;

import net.darktree.core.world.World;
import net.darktree.core.world.tile.TilePos;
import net.darktree.game.country.Symbol;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class EnclaveFinder {

	private final int[][] field;
	private final List<Enclave> enclaves = new ArrayList<>();

	private final int width, height;
	private final World world;
	private final Symbol symbol;
	private final ControlFinder control;

	private final static int[][] OFFSETS = {
			{-1, +0}, {+0, -1}, {+0, +1}, {+1, +0}
	};

	public EnclaveFinder(World world, Symbol symbol) {
		this.world = world;
		this.width = world.width;
		this.height = world.height;
		this.symbol = symbol;
		this.control = new ControlFinder(world, symbol);
		this.field = new int[this.width][this.height];

		compute();
	}

	/**
	 * Get the list of all found enclaves
	 */
	public List<Enclave> getEnclaves() {
		return enclaves;
	}

	/**
	 * Use {@link Enclave#forEachTile(Consumer)}
	 */
	void forEachEnclaveTile(int id, Consumer<TilePos> consumer) {
		for (int x = 0; x < width; x ++) {
			for (int y = 0; y < height; y ++) {
				if (field[x][y] == id) {
					consumer.accept(new TilePos(x, y));
				}
			}
		}
	}

	private void compute() {
		int id = 0;

		boolean start = true; // should a new enclave be assigned?
		boolean dirty = true; // where there any changes made during the lass iteration?
		boolean propagated; // was propagation of already assigned enclave continued?

		Enclave enclave = null;

		while (dirty) {
			dirty = false;
			propagated = false;

			for (int x = 0; x < width; x ++) {
				for (int y = 0; y < height; y ++) {
					if (field[x][y] == 0) {
						if (world.getTileState(x, y).getOwner() == symbol && !control.canControl(x, y)) {
							if (start) {
								id ++;

								field[x][y] = id;
								start = false;

								// add newly marked enclave to the list
								enclave = new Enclave(this.symbol, new TilePos(x, y), this, id);
								enclaves.add(enclave);

								// update neighbours for starting position
								for (int[] pos : OFFSETS) {
									query(x + pos[0], y + pos[1], enclave);
								}
							} else {
								propagated |= propagate(x, y, id, enclave);
							}

							dirty = true;
						}
					}
				}
			}

			// no propagation was made, but there still are some unassigned spaces
			// start marking the next enclave
			if (dirty && !propagated) {
				start = true;
			}
		}
	}

	private boolean propagate(int x, int y, int id, Enclave enclave) {
		for (int[] o1 : OFFSETS) {
			if( get(x + o1[0], y + o1[1]) ) {
				this.field[x][y] = id;

				for (int[] o2 : OFFSETS) {
					query(x + o2[0], y + o2[1], enclave);
				}

				return true;
			}
		}

		return false;
	}

	private boolean get(int x, int y) {
		if (x >= 0 && y >= 0 && x < width && y < height) {
			return this.field[x][y] != 0;
		}

		return false;
	}

	private void query(int x, int y, Enclave enclave) {
		if (x >= 0 && y >= 0 && x < width && y < height) {
			if (field[x][y] == 0) {
				Symbol owner = world.getTileState(x, y).getOwner();

				if (owner != this.symbol) {
					enclave.addNeighbour(owner);
				}
			}
		}
	}

}
