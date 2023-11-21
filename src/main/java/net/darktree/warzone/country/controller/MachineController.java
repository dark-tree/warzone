package net.darktree.warzone.country.controller;

import net.darktree.warzone.country.Controller;
import net.darktree.warzone.country.Country;
import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.country.ai.WeighedPos;
import net.darktree.warzone.country.ai.unit.UnitManager;
import net.darktree.warzone.network.packet.EndTurnPacket;
import net.darktree.warzone.util.Logger;
import net.darktree.warzone.util.Profiler;
import net.darktree.warzone.util.math.MathHelper;
import net.darktree.warzone.world.WorldSnapshot;
import net.darktree.warzone.world.action.ColonizeAction;
import net.darktree.warzone.world.action.ledger.DeferredActionQueue;
import net.darktree.warzone.world.entity.UnitEntity;
import net.darktree.warzone.world.terrain.*;
import net.darktree.warzone.world.tile.TilePos;

import java.util.List;

public class MachineController extends Controller {

	@Override
	public boolean isSelf() {
		return false;
	}

	@Override
	public void turnStart(Country country, WorldSnapshot world) {
		// TODO :jeff:
		Profiler profiler = Profiler.start();

		ColonizationFinder colonization = colonize(world, country);
		DangerFinder danger = new DangerFinder(country.symbol, world);
		PlacementFinder placement = new PlacementFinder(world, country.symbol, danger);

		DeferredActionQueue.Recorder recorder = DeferredActionQueue.record();
		List<UnitEntity> units = getUnits(country.symbol, world);
		UnitManager manager = new UnitManager(world, country);

		manager.addAllTargets(colonization.solve(false));
		manager.addAllTargets(placement.solve());
		manager.solve(recorder, units);

		recorder.bake(500, () -> new EndTurnPacket().broadcast()).replay(world.getLedger());
		profiler.print("Turn done, took %sms!");
	}

	private ColonizationFinder colonize(WorldSnapshot world, Country country) {
		PrincipalityRangeFinder radius = new PrincipalityRangeFinder(world);
		TargetFinder target = new TargetFinder(country.symbol, radius);
		ColonizationFinder colonization = new ColonizationFinder(country.symbol, world, target.getTargets(), true);

		List<WeighedPos> targets = colonization.solve(true);
		if (!targets.isEmpty()) {
			TilePos pos = WeighedPos.highest(targets);
			world.getLedger().push(new ColonizeAction(MathHelper.nextRandomDice(true), pos.x, pos.y, false));
			Logger.info("Colonized at: ", pos);

			//world.update(); // TODO
			colonization.update();
		}

		return colonization;
	}

	private List<UnitEntity> getUnits(Symbol symbol, WorldSnapshot world) {
		return world.getEntities().stream()
				.filter(entity -> entity instanceof UnitEntity)
				.map(entity -> (UnitEntity) entity)
				.filter(unit -> unit.getSymbol() == symbol)
				.filter(unit -> !unit.hasMoved())
				.toList();
	}

}
