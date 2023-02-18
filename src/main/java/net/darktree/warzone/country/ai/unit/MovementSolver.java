package net.darktree.warzone.country.ai.unit;

import net.darktree.warzone.country.ai.unit.data.UnitMove;
import net.darktree.warzone.country.ai.unit.data.UnitSource;
import net.darktree.warzone.country.ai.unit.data.UnitTarget;
import net.darktree.warzone.util.Logger;
import net.darktree.warzone.util.math.VennHelper;

import java.util.*;

public final class MovementSolver {

	/**
	 * Generate a solution for moving the available units into the requested positions
	 */
	public Solution solve(LinkedList<UnitTarget> targets, LinkedList<UnitSource> sources) {
		if (targets.isEmpty() || sources.isEmpty()) {
			return new Solution();
		}

		return vennSolverPass(targets, sources);
	}

	/**
	 * This is used after the venn pass to generate solutions for some dropped targets
	 * this algorithm is faster but has very poor solution quality
	 *
	 * @param dropped the targets to attempt recovery on
	 * @param sources the available sources list
	 * @param solution the solution move list to append to
	 */
	private void fixupSolverPass(List<UnitTarget> dropped, List<UnitSource> sources, List<UnitMove> solution) {
		if (dropped.isEmpty()) {
			return;
		}

		// keep the most important targets on top
		Collections.sort(dropped);

		int counter = 0;
		Iterator<UnitSource> sourceIterator = sources.iterator();

		while (sourceIterator.hasNext()) {
			Iterator<UnitTarget> targetIterator = dropped.iterator();
			UnitSource source = sourceIterator.next();

			while (targetIterator.hasNext()) {
				UnitTarget target = targetIterator.next();

				// if the source matches the target we need to remove the
				// target to not check it multiple times
				if (target.candidates.contains(source)) {
					solution.add(new UnitMove(target, source));
					counter ++;
					targetIterator.remove();
					sourceIterator.remove();
					break;
				}
			}
		}

		Logger.info("Unit movement solution unavailable for ", dropped.size(), " targets, rudimentary fixup pass found solution for ", counter, " of them.");
	}

	/**
	 * The main solver pass, uses Venn Diagrams to find the best solution available for the given
	 * problem, in case it fails it will fall back onto fixupSolverPass to save computation time
	 *
	 * @param targets the targets to try to link with sources
	 * @param sources the available sources list
	 */
	private Solution vennSolverPass(LinkedList<UnitTarget> targets, LinkedList<UnitSource> sources) {
		final int total = targets.size();
		Solution answer = new Solution();
		LinkedList<UnitTarget> dropped = new LinkedList<>();
		Iterator<UnitTarget> iterator = targets.iterator();

		// keep the most important targets on top
		Collections.sort(targets);

		// check which targets are reachable by which source
		for (UnitSource source : sources) {
			source.checkTargets(targets);
		}

		// get rid of unreachable targets, the AI will have to deal with them itself
		while (iterator.hasNext()) {
			UnitTarget target = iterator.next();

			if (target.isUnreachable()) {
				answer.unreachable.add(target);
				iterator.remove();
			}
		}

		// remove the least important targets if we have more targets than sources
		while (targets.size() > sources.size()) {
			dropped.add(targets.pollLast());
		}

		// reformat the data
		List<Set<UnitSource>> sets = new ArrayList<>();
		for (UnitTarget target : targets) {
			sets.add(target.candidates);
		}

		// try until a solution is found or all targets are dropped (not good)
		while (!targets.isEmpty()) {

			// venn math magic
			List<List<UnitSource>> solutions = VennHelper.findAllUniquePicks(sets);
			Logger.info("Found ", solutions.size(), " valid solutions for a subset of size ", targets.size(), ", total problem size: ", total, ".");

			// no fully valid solution found
			if (solutions.isEmpty()) {
				dropped.add(targets.pollLast());
			} else {
				List<UnitMove> solution = pickBestSolution(packSolutions(solutions, targets));

				// now remove all used sources and targets
				for (UnitMove move : solution) {
					move.removeFromSearch(sources, targets);
				}

				// try to do something with dropped targets
				fixupSolverPass(dropped, sources, solution);

				answer.moves.addAll(solution);
				answer.unreachable.addAll(dropped);
//				Logger.info("Answer available for ", answer.moves.size(), " targets (", answer.unreachable.size(), " unreachable).");

				return answer;
			}

		}

		// uh oh, venn has failed us!
		Logger.warn("Failed to find a valid unit movement solution for any subset of ", total ," targets!");
		answer.unreachable.addAll(dropped);

		return answer;
	}

	/**
	 * Pack the solution set returned by VennHelper
	 * into a more useful format
	 */
	private List<List<UnitMove>> packSolutions(List<List<UnitSource>> solutions, List<UnitTarget> targets) {
		List<List<UnitMove>> packed = new ArrayList<>();

		for (List<UnitSource> solution : solutions) {
			List<UnitMove> moves = new ArrayList<>();
			int index = 0;

			for (UnitTarget target : targets) {
				moves.add(new UnitMove(target, solution.get(index++)));
			}

			packed.add(moves);
		}

		return packed;
	}

	/**
	 * Picks the solution with the lowest amount of movement
	 * steps, for a more compelling visual effect
	 */
	private List<UnitMove> pickBestSolution(List<List<UnitMove>> solutions) {
		int min = 0;
		List<UnitMove> selected = null;

		// minimize the damage
		if (solutions.size() > 30000) {
			Logger.warn("Solution picker overloaded, picking at random to save time...");
			return solutions.get(0);
		}

		for (List<UnitMove> solution : solutions) {
			int total = 0;

			for (UnitMove move : solution) {
				total += move.getSteps();
			}

			if (selected == null || min > total) {
				selected = solution;
				min = total;
			}
		}

		return selected;
	}

	public static class Solution {
		public final LinkedList<UnitMove> moves = new LinkedList<>();
		public final LinkedList<UnitTarget> unreachable = new LinkedList<>();

//		private void reorder(World world) {
//			Map<TilePos, Boolean> real = new HashMap<>(moves.size());
//
//			for (Move move : moves) {
//				real.put(move.target, move.target.isOccupied(world));
//			}
//
//			boolean sorted = false;
//
//			while (!sorted) {
//				Map<TilePos, Boolean> states = new HashMap<>(real);
//				sorted = true;
//
//				for (Move move : moves) {
//					states.put(move.source, Boolean.FALSE);
//
//					if (states.get(move.target)) {
//						moves.remove(move);
//						moves.add(0, move);
//						sorted = false;
//						break;
//					}
//
//					states.put(move.target, Boolean.TRUE);
//				}
//			}
//		}
	}

}
