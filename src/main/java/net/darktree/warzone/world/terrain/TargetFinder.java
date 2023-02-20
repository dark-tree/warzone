package net.darktree.warzone.world.terrain;

import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.country.ai.WeighedPos;
import net.darktree.warzone.util.math.MathHelper;
import net.darktree.warzone.world.pattern.Patterns;
import net.darktree.warzone.world.tile.tiles.Tiles;

import java.util.ArrayList;
import java.util.List;

public class TargetFinder extends AbstractFinder {

	private final Symbol symbol;
	private final PrincipalityRangeFinder finder;
	private final List<WeighedPos> targets = new ArrayList<>();

	public TargetFinder(Symbol symbol, PrincipalityRangeFinder finder) {
		super(Patterns.NEIGHBOURS, finder.world);
		this.finder = finder;
		this.symbol = symbol;

		compute();
	}

	/**
	 * Return a list of sorted targets
	 */
	public List<WeighedPos> getTargets() {
		return targets;
	}

	private void compute() {
		targets.clear();
		List<WeighedPos> extra = new ArrayList<>();

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (finder.getPrincipality(x, y) == symbol && isValidTarget(x, y)) {
					if (getOwner(x, y) == Symbol.NONE) {
						targets.add(new WeighedPos(x, y, getTargetWeight(x, y)));
					}

					Patterns.RING.iterate(world, x, y, pos -> {
						if (getOwner(pos.x, pos.y) == Symbol.NONE && getTile(pos.x, pos.y).canColonize(symbol)) {
							extra.add(WeighedPos.wrap(pos, 0));
						}
					});
				}
			}
		}

		targets.addAll(extra);
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
		return 5 - MathHelper.clamp(finder.getPrincipalityRange(x, y), 0, 4);
	}

}
