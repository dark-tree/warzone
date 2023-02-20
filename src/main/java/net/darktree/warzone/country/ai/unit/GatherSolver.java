package net.darktree.warzone.country.ai.unit;

import net.darktree.warzone.country.ai.unit.data.UnitAvoidField;
import net.darktree.warzone.country.ai.unit.data.UnitMove;
import net.darktree.warzone.country.ai.unit.data.UnitSource;
import net.darktree.warzone.country.ai.unit.data.UnitTarget;
import net.darktree.warzone.util.Logger;
import net.darktree.warzone.util.iterable.SpiralIterable;
import net.darktree.warzone.world.path.Path;
import net.darktree.warzone.world.path.PathFinder;
import net.darktree.warzone.world.tile.TilePos;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public final class GatherSolver {

	public void solve(List<UnitMove> moves, UnitAvoidField avoid, LinkedList<UnitTarget> targets, UnitTarget gather, List<UnitSource> sources) {
		Iterator<UnitSource> iterator = sources.iterator();

		while (iterator.hasNext()) {
			UnitSource source = iterator.next();
			UnitTarget selected = null;
			int distance = 0;

			for (UnitTarget target : targets) {
				int tiles = target.manhattan(source);

				if (selected == null || tiles < distance) {
					selected = target;
					distance = tiles;
				}
			}

			// make the move
			if (selected != null) {
				if (attract(moves, avoid, selected, source)) {
					targets.remove(selected);
					iterator.remove();
				}
			}
		}

		// for all remaining units
		if (gather != null) {
			iterator = sources.iterator();

			while (iterator.hasNext()) {
				UnitSource source = iterator.next();

				if (attract(moves, avoid, gather, source)) {
					iterator.remove();
				}
			}
		}
	}

	private boolean attract(List<UnitMove> moves, UnitAvoidField avoid, UnitTarget target, UnitSource source) {
		PathFinder finder = source.unit.getPathFinder(true);

		if (!finder.canPossiblyReach(target.x, target.y)) {
			Logger.warn("Failed to gather one unit, can't find a way!");
			return false;
		}

		Path path = finder.getPathProvider(target.x, target.y).getValidPath();
		if (path == null) {
			Logger.warn("Failed to gather one unit, no valid path!");
			return false;
		}

		TilePos end = path.getEnd();
		int avoidance = avoid.get(end.x, end.y);

		// uh oh not great
		if (avoidance != 0) {
			boolean escaped = false;

			for (SpiralIterable.SpiralPoint point : SpiralIterable.of(1, end.x, end.y)) {
				if (finder.canReach(point.x, point.y) && avoid.get(point.x, point.y) == 0) {
					end = point.asTilePos();
					escaped = true;
					break;
				}
			}

			if (!escaped && avoidance > target.weight) {
				Logger.warn("Failed to gather one unit, blocked by avoidance!");
				return false;
			}
		}

		UnitTarget partial = new UnitTarget(end.x, end.y, target.weight);
		moves.add(new UnitMove(partial, source));
		return true;
	}

}
