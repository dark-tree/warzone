package net.darktree.warzone.world.terrain;

import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.world.tile.TilePos;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class Enclave {

	public final Symbol owner;
	public final TilePos pos;

	private final EnclaveFinder finder;
	private final int id;
	private final Set<Symbol> neighbours = new HashSet<>();

	public Enclave(Symbol owner, TilePos pos, EnclaveFinder finder, int id) {
		this.owner = owner;
		this.pos = pos;
		this.finder = finder;
		this.id = id;
	}

	void addNeighbour(Symbol owner) {
		neighbours.add(owner);
	}

	/**
	 * Returns the symbol of a country the encircles this enclave,
	 * if the enclave is not encircled (has more than one neighbour) it returns null
	 */
	public Symbol encircled() {
		if (neighbours.size() == 1) {
			return neighbours.stream().findFirst().get();
		}

		return null;
	}

	/**
	 * Iterate over the position of all tiles in this enclave
	 */
	public void forEachTile(Consumer<TilePos> consumer) {
		this.finder.forEachEnclaveTile(this.id, consumer);
	}

	/**
	 * Get a set of the symbols of the neighbours this enclave has
	 */
	public Set<Symbol> getNeighbours() {
		return neighbours;
	}

}
