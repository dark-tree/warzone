package net.darktree.core.world.terrain;

import net.darktree.core.world.World;
import net.darktree.core.world.tile.TilePos;
import net.darktree.game.country.Symbol;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class EnclaveFinder {

	private final int[][] field;
	private final int[][] marks;

	private final List<Enclave> enclaves = new ArrayList<>();

	private final int width, height;
	private final World world;
	private final ControlFinder control;

	private final static int[][] OFFSETS = {
			{-1, +0}, {+0, -1}, {+0, +1}, {+1, +0}
	};

	public EnclaveFinder(World world, ControlFinder control) {
		this.world = world;
		this.width = world.width;
		this.height = world.height;
		this.control = control;
		this.field = new int[this.width][this.height];
		this.marks = new int[this.width][this.height];

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
				if (marks[x][y] == id) {
					consumer.accept(new TilePos(x, y));
				}
			}
		}
	}

	private void compute() {
		int id = 0;

		while(true) {
			id ++;

			// try finding another enclave
			Enclave enclave = initializer(id);
			if (enclave == null) break;

			int level = 1;

			// propagate current enclave until there are no more tiles
			for (boolean dirty = true; dirty; level ++) {
				dirty = false;

				for (int x = 0; x < width; x++) {
					for (int y = 0; y < height; y++) {
						if (field[x][y] == level) {
							dirty = true;
							propagate(x, y, level + 1, id, enclave);
						}
					}
				}
			}

		}
	}

	private Enclave initializer(int id) {
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				field[x][y] = 0;
			}
		}

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (marks[x][y] == 0 && !control.canControl(x, y)) {

					field[x][y] = 1;
					marks[x][y] = id;

					// add newly marked enclave to the list
					Enclave enclave = new Enclave(world.getTileState(x, y).getOwner(), new TilePos(x, y), this, id);
					enclaves.add(enclave);

					return enclave;
				}
			}
		}

		// no enclaves found
		return null;
	}

	private void propagate(int x, int y, int value, int id, Enclave enclave) {
		for (int[] pair : OFFSETS) {
			set(x + pair[0], y + pair[1], value, id, enclave);
		}
	}

	private void set(int x, int y, int value, int id, Enclave enclave) {
		if (x >= 0 && y >= 0 && x < width && y < height) {
			if (this.field[x][y] == 0) {
				Symbol symbol = world.getTileState(x, y).getOwner();

				if (enclave.owner == symbol) {
					this.field[x][y] = value;
					this.marks[x][y] = id;
				}else{
					enclave.addNeighbour(symbol);
				}
			}
		}
	}

}
