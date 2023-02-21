package net.darktree.warzone.country.ai.unit.data;

import net.darktree.warzone.world.entity.UnitEntity;
import net.darktree.warzone.world.path.PathFinder;
import net.darktree.warzone.world.tile.TilePos;

import java.util.List;

public final class UnitSource extends TilePos {

	public final UnitEntity unit;
	public final PathFinder finder;

	public UnitSource(UnitEntity unit) {
		super(unit.getX(), unit.getY());
		this.unit = unit;
		this.finder = unit.getPathFinder(false);
	}

	/**
	 * Add itself as a candidate to every target that is in range from the given list
	 */
	public void checkTargets(List<UnitTarget> targets) {
		for (UnitTarget target : targets) {
			if (finder.canReach(target.x, target.y) || (target.x == this.x && target.y == this.y)) {
				target.candidates.add(this);
			}
		}
	}

}
