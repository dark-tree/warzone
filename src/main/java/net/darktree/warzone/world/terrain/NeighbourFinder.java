package net.darktree.warzone.world.terrain;

import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.util.Util;
import net.darktree.warzone.world.World;
import net.darktree.warzone.world.pattern.Patterns;
import net.darktree.warzone.world.tile.TilePos;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class NeighbourFinder extends AbstractFinder {

	private final Map<Symbol, Set<Symbol>> neighbours = Util.enumMapOf(Symbol.class, symbol -> new HashSet<>());

	public NeighbourFinder(World world) {
		super(Patterns.NEIGHBOURS, world);

		compute();
	}

	public Set<Symbol> getNeighbours(Symbol symbol) {
		return neighbours.get(symbol);
	}

	private void compute() {
		for (int x = 0; x < width; x ++) {
			for (int y = 0; y < height; y ++) {
				addNeighbours(x, y, world.getPrincipality(x, y));
			}
		}
	}

	private void addNeighbours(int x, int y, Symbol symbol) {
		for (TilePos offset : offsets) {
			addNeighbour(x + offset.x, y + offset.y, symbol);
		}
	}

	private void addNeighbour(int x, int y, Symbol symbol) {
		if (isPosValid(x, y)) {
			Symbol neighbour = world.getPrincipality(x, y);

			if (symbol != neighbour && neighbour != Symbol.NONE) {
				neighbours.get(symbol).add(neighbour);
			}
		}
	}

}
