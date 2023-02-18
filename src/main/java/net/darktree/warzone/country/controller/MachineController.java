package net.darktree.warzone.country.controller;

import net.darktree.warzone.country.Controller;
import net.darktree.warzone.country.Country;
import net.darktree.warzone.country.ai.TicketSolver;
import net.darktree.warzone.network.packet.EndTurnPacket;
import net.darktree.warzone.util.Logger;
import net.darktree.warzone.world.World;
import net.darktree.warzone.world.action.ColonizeAction;
import net.darktree.warzone.world.action.manager.DeferredActionQueue;
import net.darktree.warzone.world.terrain.ColonizationFinder;
import net.darktree.warzone.world.terrain.PrincipalityRangeFinder;
import net.darktree.warzone.world.terrain.TargetFinder;
import net.darktree.warzone.world.tile.TilePos;

import java.util.List;

public class MachineController extends Controller {

	@Override
	public boolean isSelf() {
		return false;
	}

	@Override
	public void turnStart(Country country, World world) {
		// TODO :jeff:

		ColonizationFinder colonization = colonize(world, country);

		DeferredActionQueue.Recorder recorder = DeferredActionQueue.record();
		TicketSolver solver = new TicketSolver(country, world);

		List<TilePos> tickets = colonization.solve(false);
		solver.submit(tickets);
		solver.solve(recorder);

		recorder.bake(country.symbol, 500, () -> new EndTurnPacket().broadcast()).replay(world.getManager());
	}

	private ColonizationFinder colonize(World world, Country country) {
		PrincipalityRangeFinder radius = new PrincipalityRangeFinder(world);
		TargetFinder target = new TargetFinder(country.symbol, radius);
		ColonizationFinder colonization = new ColonizationFinder(country.symbol, world, target.getTargets(), true);

		List<TilePos> targets = colonization.solve(true);
		if (!targets.isEmpty()) {
			TilePos pos = targets.get(0);
			world.getManager().apply(new ColonizeAction(world, 6, pos.x, pos.y, false));
			Logger.info("Colonized at: ", pos);
		}

		world.update();
		colonization.update();
		return colonization;
	}

}
