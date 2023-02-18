package net.darktree.warzone.country.ai.unit;

import net.darktree.warzone.country.ai.unit.data.UnitMove;
import net.darktree.warzone.country.ai.unit.data.UnitSource;
import net.darktree.warzone.country.ai.unit.data.UnitTarget;
import net.darktree.warzone.util.Logger;
import net.darktree.warzone.world.path.Path;
import net.darktree.warzone.world.path.PathFinder;
import net.darktree.warzone.world.tile.TilePos;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public final class GatherSolver {

	public void solve(List<UnitMove> moves, LinkedList<UnitTarget> targets, UnitTarget gather, List<UnitSource> sources) {
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
				if (attract(moves, selected, source)) {
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

				if (attract(moves, gather, source)) {
					iterator.remove();
				}
			}
		}
	}

	private boolean attract(List<UnitMove> moves, UnitTarget target, UnitSource source) {
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
		UnitTarget partial = new UnitTarget(end.x, end.y, target.weight);
		moves.add(new UnitMove(partial, source));
		return true;
	}

}
