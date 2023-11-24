package net.darktree.warzone.country.ai.unit;

import net.darktree.warzone.country.Country;
import net.darktree.warzone.country.ai.WeighedPos;
import net.darktree.warzone.country.ai.unit.data.*;
import net.darktree.warzone.world.WorldSnapshot;
import net.darktree.warzone.world.action.SummonAction;
import net.darktree.warzone.world.action.ledger.DeferredActionQueue;
import net.darktree.warzone.world.entity.UnitEntity;
import net.darktree.warzone.world.tile.TilePos;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public final class UnitManager {

	private final WorldSnapshot world;
	private final Country country;

	private final UnitAvoidField avoid;
	private final MovementSolver movementSolver;
	private final SummonSolver summonSolver;
	private final GatherSolver gatherSolver;
	private final AvoidanceSolver avoidanceSolver;

	private final LinkedList<UnitTarget> targets = new LinkedList<>();
	private UnitTarget gather = null;

	public UnitManager(WorldSnapshot world, Country country) {
		this.world = world;
		this.country = country;
		this.avoid = new UnitAvoidField(world.getInfo());
		this.movementSolver = new MovementSolver();
		this.summonSolver = new SummonSolver(world, country);
		this.gatherSolver = new GatherSolver();
		this.avoidanceSolver = new AvoidanceSolver();
	}

	/**
	 * Add a list of targets to place a unit on, the targets should to be unique
	 */
	public void addAllTargets(List<WeighedPos> targets) {
		for (WeighedPos target : targets) {
			addTarget(target.x, target.y, target.weight);
		}
	}

	/**
	 * Add a target to place a unit on, the target should to be unique
	 */
	public void addTarget(int x, int y, int weight) {
		WeighedPos prev = targets.stream().filter(target -> target.equals(x, y)).findAny().orElse(null);
		UnitTarget target = new UnitTarget(x, y, weight);

		if (prev == null) {
			targets.add(target);
			return;
		}

		if (prev.weight < weight) {
			targets.remove(prev);
			targets.add(target);
		}
	}

	/**
	 * Add a tile that should be avoided by units
	 */
	public void addAvoid(int x, int y, int weight) {
		avoid.set(x, y, weight);
	}

	/**
	 * Set a gathering point, all non-busy units will try to go towards it
	 */
	public void setGather(int x, int y, int weight) {
		if (gather == null || gather.weight < weight) {
			this.gather = new UnitTarget(x, y, weight);
		}
	}

	/**
	 * Reset all submitted data
	 */
	public void reset() {
		targets.clear();
		avoid.clear();
		gather = null;
	}

	/**
	 * Get a solution for the submitted requirements
	 */
	public void solve(DeferredActionQueue.Recorder recorder, List<UnitEntity> units) {
		LinkedList<UnitSource> sources = units.stream().map(UnitSource::new).collect(Collectors.toCollection(LinkedList::new));

		// obtain basic solution
		UnitSolution solution = movementSolver.solve(targets, sources);

		// add new unit at any unreachable spot or randomly
		summonSolverPass(recorder, solution, true);

		// move units towards unreachable spaces
		finalSolverPass(solution, sources);

		// skip pinned moves and submit
		for (UnitMove move : solution.moves) {
			if (!move.isPinned()) {
				recorder.push(move.asAction());
			}
		}
	}

	private void summonSolverPass(DeferredActionQueue.Recorder recorder, UnitSolution solution, boolean fallback) {
		TilePos pos = summonSolver.placeAtBestOf(solution.unreachable, fallback);

		if (pos != null) {
			for (UnitMove move : solution.moves) {
				if (move.target.x == pos.x && move.target.y == pos.y) {
					pos = null;
					break;
				}
			}
		}

		if (pos != null) {
			SummonAction action = new SummonAction(country.getCapitol().getX(), country.getCapitol().getY(), pos.x, pos.y);
			recorder.push(action);
		}
	}

	private void finalSolverPass(UnitSolution solution, List<UnitSource> sources) {
		gatherSolver.solve(solution.moves, avoid, solution.unreachable, gather, sources);
		avoidanceSolver.solve(world, avoid, solution.moves, sources);
	}

}
