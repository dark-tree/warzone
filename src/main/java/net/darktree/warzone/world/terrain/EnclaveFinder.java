package net.darktree.warzone.world.terrain;

import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.world.WorldSnapshot;
import net.darktree.warzone.world.pattern.Patterns;
import net.darktree.warzone.world.tile.TilePos;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class EnclaveFinder extends AbstractFieldFinder {

	private final int[][] marks;

	private final List<Enclave> enclaves = new ArrayList<>();
	private final ControlFinder control;

	public EnclaveFinder(WorldSnapshot world, ControlFinder control) {
		super(Patterns.RING, world);

		this.control = control;
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

	protected void compute() {
		int id = 0;

		while(true) {
			id ++;

			// try finding another enclave
			Enclave enclave = findEnclave(id);
			if (enclave == null) break;

			// propagate current enclave until there are no more tiles
			final int fid = id;
			iterate((x, y, level) -> propagate(x, y, level, fid, enclave));
		}
	}

	private Enclave findEnclave(int id) {
		clearField(this.field);

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (marks[x][y] == 0 && !control.canControl(x, y)) {

					field[x][y] = 1;
					marks[x][y] = id;

					// add newly marked enclave to the list
					Enclave enclave = new Enclave(getOwner(x, y), new TilePos(x, y), this, id);
					enclaves.add(enclave);

					return enclave;
				}
			}
		}

		// no enclaves found
		return null;
	}

	private void propagate(int x, int y, int value, int id, Enclave enclave) {
		for (TilePos offset : offsets) {
			set(x + offset.x, y + offset.y, value, id, enclave);
		}
	}

	private void set(int x, int y, int value, int id, Enclave enclave) {
		if (!isPosValid(x, y)) {
			return;
		}

		if (this.field[x][y] == 0) {
			Symbol owner = getOwner(x, y);

			if (enclave.owner == owner) {
				this.field[x][y] = value;
				this.marks[x][y] = id;
			}else{
				enclave.addNeighbour(owner);
			}
		}
	}

}
