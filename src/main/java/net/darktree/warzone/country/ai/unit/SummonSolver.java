package net.darktree.warzone.country.ai.unit;

import net.darktree.warzone.country.Country;
import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.country.ai.unit.data.UnitTarget;
import net.darktree.warzone.util.math.MathHelper;
import net.darktree.warzone.world.WorldInfo;
import net.darktree.warzone.world.WorldSnapshot;
import net.darktree.warzone.world.entity.building.CapitolBuilding;
import net.darktree.warzone.world.path.PathFinder;
import net.darktree.warzone.world.tile.TilePos;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class SummonSolver {

	private final WorldSnapshot world;
	private final CapitolBuilding capitol;
	private final PathFinder finder;

	public SummonSolver(WorldSnapshot world, Country country) {
		this.world = world;
		this.capitol = country.getCapitol();
		this.finder = capitol.getPathFinder();
	}

	/**
	 * Place a new unit on the best tile in given list
	 * If that can't be done and fallback is enabled places a new unit randomly
	 */
	public TilePos placeAtBestOf(List<UnitTarget> targets, boolean fallback) {
		Iterator<UnitTarget> iterator = targets.iterator();

		// try placing at one of the given positions
		while (iterator.hasNext()) {
			UnitTarget target = iterator.next();

			if (finder.canReach(target.x, target.y)) {
				iterator.remove();
				return new TilePos(target.x, target.y);
			}
		}

		// if that fails try to make some position up
		if (fallback) {
			List<TilePos> tiles = new ArrayList<>();
			Symbol symbol = capitol.getSymbol();
			WorldInfo info = world.getInfo();

			for (int x = 0; x < info.width; x ++) {
				for (int y = 0; y < info.height; y ++) {
					if (finder.canReach(x, y) && world.canControl(x, y, symbol)) {
						tiles.add(new TilePos(x, y));
					}
				}
			}

			return MathHelper.randomListPick(tiles);
		}

		return null;
	}

}
