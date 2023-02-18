package net.darktree.warzone.country.ai.unit;

import net.darktree.warzone.country.Country;
import net.darktree.warzone.country.ai.unit.data.UnitMove;
import net.darktree.warzone.country.ai.unit.data.UnitSource;
import net.darktree.warzone.country.ai.unit.data.UnitTarget;
import net.darktree.warzone.util.Logger;
import net.darktree.warzone.world.World;
import net.darktree.warzone.world.action.SummonAction;
import net.darktree.warzone.world.action.manager.DeferredActionQueue;
import net.darktree.warzone.world.entity.UnitEntity;
import net.darktree.warzone.world.tile.TilePos;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public final class UnitManager {

	private final World world;
	private final Country country;

	private final MovementSolver movementSolver;
	private final SummonSolver summonSolver;
	private final GatherSolver gatherSolver;

	private final LinkedList<UnitTarget> targets = new LinkedList<>();
	private UnitTarget gather = null;

	public UnitManager(World world, Country country) {
		this.world = world;
		this.country = country;
		this.movementSolver = new MovementSolver();
		this.summonSolver = new SummonSolver(world, country);
		this.gatherSolver = new GatherSolver();
	}

	/**
	 * Add a target to place a unit on, the target should to be unique
	 */
	public void addTarget(int x, int y, int weight) {
		for (UnitTarget target : targets) {
			if (target.equals(x, y)) {
				Logger.info("Non unique movement target was found and discarded!");
				return;
			}
		}

		targets.add(new UnitTarget(x, y, weight));
	}

	/**
	 * Add a tile that should be avoided by units
	 */
	public void addAvoid(int x, int y, int weight) {

	}

	/**
	 * Set a gathering point, all non-busy units will try to go towards it
	 */
	public void setGather(int x, int y, int weight) {
		if (gather == null || gather.weight < weight) {
			this.gather = new UnitTarget(x, y, weight);
		}
	}

	public void solve(DeferredActionQueue.Recorder recorder, List<UnitEntity> units) {
		LinkedList<UnitSource> sources = units.stream().map(UnitSource::new).collect(Collectors.toCollection(LinkedList::new));

		// TODO remove
		world.getBorder().getBorderTiles(country.symbol).forEach(pos -> {
			addTarget(pos.x, pos.y, 1);
		});

		// obtain basic solution
		MovementSolver.Solution solution = movementSolver.solve(targets, sources);

		// add new unit at any unreachable spot or randomly
		summonSolverPass(recorder, solution, true);

		// move units towards unreachable spaces
		gatherSolverPass(solution, sources);

		// skip pinned moves and submit
		for (UnitMove move : solution.moves) {
			if (!move.isPinned()) {
				recorder.push(move.asAction(world));
			}
		}
	}

	private void summonSolverPass(DeferredActionQueue.Recorder recorder, MovementSolver.Solution solution, boolean fallback) {
		TilePos pos = summonSolver.placeAtAnyOf(solution.unreachable, fallback);

		if (pos != null) {
			for (UnitMove move : solution.moves) {
				if (move.target.x == pos.x && move.target.y == pos.y) {
					pos = null;
					break;
				}
			}
		}

		if (pos != null) {
			SummonAction action = new SummonAction(world, country.getCapitol().getX(), country.getCapitol().getY());
			action.setTarget(pos.x, pos.y);
			recorder.push(action);
		}
	}

	private void gatherSolverPass(MovementSolver.Solution solution, List<UnitSource> sources) {
		gatherSolver.solve(solution.moves, solution.unreachable, gather, sources);
	}

}
