package net.darktree.warzone.country.ai.unit;

import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.country.ai.unit.data.UnitAvoidField;
import net.darktree.warzone.country.ai.unit.data.UnitMove;
import net.darktree.warzone.country.ai.unit.data.UnitSource;
import net.darktree.warzone.country.ai.unit.data.UnitTarget;
import net.darktree.warzone.util.Logger;
import net.darktree.warzone.util.iterable.SpiralIterable;
import net.darktree.warzone.world.World;

import java.util.List;

public final class AvoidanceSolver {

	/**
	 * Use to move any units that didn't yet move in this turn
	 * to a safer position (escape avoidance zones)
	 */
	public void solve(World world, UnitAvoidField field, List<UnitMove> moves, List<UnitSource> sources) {
		for (UnitSource source : sources) {
			checkPlacement(world, field, moves, source);
		}
	}

	/**
	 * Check if the given unit can ble placed at its current postion, if not
	 * try moving it
	 */
	private void checkPlacement(World world, UnitAvoidField field, List<UnitMove> moves, UnitSource source) {
		final int value = field.get(source.x, source.y);

		if (value != 0) {
			escape(world, field, moves, source, value);
		}
	}

	/**
	 * Escape with a given unit into a safer place
	 * TODO: allow escaping into lower-avoidance
	 */
	private void escape(World world, UnitAvoidField field, List<UnitMove> moves, UnitSource source, int weight) {
		Symbol from = world.getTileState(source).getOwner();

		for (SpiralIterable.SpiralPoint point : SpiralIterable.of(5, source.x, source.y)) {
			if (field.get(point.x, point.y) == 0 && source.finder.canReach(point.x, point.y)) {
				Symbol to = world.getTileState(point).getOwner();

				if (from == to) {
					moves.add(new UnitMove(new UnitTarget(point.x, point.y, weight), source));
					return;
				}

				// TODO: in the future consider escaping to befriended states
			}
		}

		Logger.warn("Unable to escape from avoidance area!");
	}

}
