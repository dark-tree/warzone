package net.darktree.warzone.country.controller;

import net.darktree.warzone.country.Controller;
import net.darktree.warzone.country.Country;
import net.darktree.warzone.country.ai.TicketSolver;
import net.darktree.warzone.country.ai.WeighedPos;
import net.darktree.warzone.network.packet.EndTurnPacket;
import net.darktree.warzone.util.Logger;
import net.darktree.warzone.util.Profiler;
import net.darktree.warzone.util.math.MathHelper;
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
		Profiler profiler = Profiler.start();

		ColonizationFinder colonization = colonize(world, country);

		DeferredActionQueue.Recorder recorder = DeferredActionQueue.record();
		TicketSolver solver = new TicketSolver(country, world);

		List<WeighedPos> tickets = colonization.solve(false);
		solver.submit(tickets);
		solver.solve(recorder);

		recorder.bake(country.symbol, 500, () -> new EndTurnPacket().broadcast()).replay(world.getManager());
		profiler.print("Turn done, took %sms!");
	}

	private ColonizationFinder colonize(World world, Country country) {
		PrincipalityRangeFinder radius = new PrincipalityRangeFinder(world);
		TargetFinder target = new TargetFinder(country.symbol, radius);
		ColonizationFinder colonization = new ColonizationFinder(country.symbol, world, target.getTargets(), true);

		List<WeighedPos> targets = colonization.solve(true);
		if (!targets.isEmpty()) {
			TilePos pos = WeighedPos.highest(targets);
			world.getManager().apply(new ColonizeAction(world, MathHelper.nextRandomDice(), pos.x, pos.y, false));
			Logger.info("Colonized at: ", pos);

			world.update();
			colonization.update();
		}

		return colonization;
	}

}
