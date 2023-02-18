package net.darktree.warzone.country.ai.unit;

import net.darktree.warzone.country.Country;
import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.country.ai.unit.data.UnitTarget;
import net.darktree.warzone.util.math.MathHelper;
import net.darktree.warzone.world.World;
import net.darktree.warzone.world.entity.building.CapitolBuilding;
import net.darktree.warzone.world.path.PathFinder;
import net.darktree.warzone.world.tile.TilePos;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class SummonSolver {

	private final World world;
	private final CapitolBuilding capitol;
	private final PathFinder finder;

	public SummonSolver(World world, Country country) {
		this.world = world;
		this.capitol = country.getCapitol();
		this.finder = capitol.getPathFinder();
	}

	public TilePos placeAtAnyOf(List<UnitTarget> targets, boolean fallback) {
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

			for (int x = 0; x < world.getWidth(); x ++) {
				for (int y = 0; y < world.getHeight(); y ++) {
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
