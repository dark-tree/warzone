package net.darktree.warzone.country.ai;

import net.darktree.warzone.country.Country;
import net.darktree.warzone.country.ai.unit.UnitManager;
import net.darktree.warzone.world.World;
import net.darktree.warzone.world.action.manager.DeferredActionQueue;
import net.darktree.warzone.world.entity.UnitEntity;
import net.darktree.warzone.world.tile.TilePos;

import java.util.List;

@Deprecated
public class TicketSolver {

	private final UnitManager manager;
	private final World world;
	private final Country country;

	public TicketSolver(Country country, World world) {
		this.manager = new UnitManager(world, country);
		this.world = world;
		this.country = country;
	}

	public void submit(List<TilePos> tickets) {
		tickets.forEach(pos -> {
			manager.addTarget(pos.x, pos.y, 10);
		});
	}

	public void solve(DeferredActionQueue.Recorder recorder) {
		List<UnitEntity> units = world.getEntities().stream()
				.filter(entity -> entity instanceof UnitEntity)
				.map(entity -> (UnitEntity) entity)
				.filter(unit -> unit.getSymbol() == country.symbol)
				.filter(unit -> !unit.hasMoved())
				.toList();

		manager.solve(recorder, units);
	}

}
