package net.darktree.warzone.world.path;

import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.world.entity.UnitEntity;
import net.darktree.warzone.world.tile.Surface;

public class PathFinderConfig {

	public static final PathFinderConfig UNIT_SUMMON = new PathFinderConfig(10, Surface.LAND, SearchBoundary.WITHIN);

	public final int steps;
	public final Surface surface;
	public final SearchBoundary boundary;

	protected PathFinderConfig(int steps, Surface surface, SearchBoundary boundary) {
		this.steps = steps;
		this.surface = surface;
		this.boundary = boundary;
	}

	/**
	 * Get a pathfinder config for a given unit
	 *
	 * @param unit the unit for which to pathfind
	 * @param owner the owner of the tile this unit stands on
	 */
	public static PathFinderConfig getForUnitAt(UnitEntity unit, Symbol owner) {
		return new PathFinderConfig(5, Surface.LAND, SearchBoundary.getForUnitAt(unit.armored, owner));
	}

}
