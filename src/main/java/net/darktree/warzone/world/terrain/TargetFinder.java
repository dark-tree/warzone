package net.darktree.warzone.world.terrain;

import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.world.pattern.Patterns;
import net.darktree.warzone.world.tile.TilePos;
import net.darktree.warzone.world.tile.tiles.Tiles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TargetFinder extends AbstractFinder {

	private final Symbol symbol;
	private final PrincipalityRangeFinder finder;
	private final List<TilePos> targets = new ArrayList<>();

	public TargetFinder(Symbol symbol, PrincipalityRangeFinder finder) {
		super(Patterns.NEIGHBOURS, finder.world);
		this.finder = finder;
		this.symbol = symbol;

		compute();
	}

	/**
	 * Return a list of sorted targets
	 */
	public List<TilePos> getTargets() {
		return targets;
	}

	private void compute() {
		targets.clear();
		List<TilePos> extra = new ArrayList<>();

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (finder.getPrincipality(x, y) == symbol && isValidTarget(x, y)) {
					if (getOwner(x, y) == Symbol.NONE) {
						targets.add(new TilePos(x, y));
					}

					Patterns.RING.iterate(world, x, y, pos -> {
						if (getOwner(pos.x, pos.y) == Symbol.NONE && getTile(pos.x, pos.y).canColonize(symbol)) {
							extra.add(pos);
						}
					});
				}
			}
		}

		targets.sort(new TargetSorter());
		Collections.reverse(targets);
		targets.addAll(extra);
		Collections.reverse(targets);
	}

	/**
	 * Checks if the given tile should be targeted by the AI
	 */
	private boolean isValidTarget(int x, int y) {
		return getTile(x, y) == Tiles.MATERIAL_ORE;
	}

	/**
	 * Returns the importance of this target, smaller = more important
	 */
	private int getTargetWeight(int x, int y) {
		return finder.getPrincipalityRange(x, y) + 10;
	}

	private class TargetSorter implements Comparator<TilePos> {

		public int compare(TilePos pos1, TilePos pos2){
			final int a = getTargetWeight(pos1.x, pos1.y);
			final int b = getTargetWeight(pos2.x, pos2.y);

			return Integer.compare(a, b);
		}

	}

}
