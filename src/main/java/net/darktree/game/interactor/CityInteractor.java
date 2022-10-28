package net.darktree.game.interactor;

import net.darktree.Main;
import net.darktree.core.client.render.vertex.VertexBuffer;
import net.darktree.core.world.World;
import net.darktree.core.world.WorldHolder;
import net.darktree.core.world.action.SummonAction;
import net.darktree.core.world.overlay.PathfinderOverlay;
import net.darktree.core.world.path.Pathfinder;
import net.darktree.game.buildings.Building;
import net.darktree.game.buildings.CapitolBuilding;
import net.darktree.game.country.Symbol;

public class CityInteractor extends Interactor {

	private final Pathfinder pathfinder;
	private final World world;
	private final Building building;

	public CityInteractor(Symbol symbol, World world) {
		this.building = world.getCountry(symbol).getCapitol();
		this.pathfinder = new Pathfinder(world, 10, symbol, consumer -> building.forEachTile(consumer), true);
		this.world = world;

		world.setOverlay(new PathfinderOverlay(pathfinder));
	}

	@Override
	public void draw(VertexBuffer buffer) {
		int x = Main.window.input().getMouseMapX(world.getView());
		int y = Main.window.input().getMouseMapY(world.getView());

		if (world.isPositionValid(x, y) && pathfinder.canReach(x, y)) {
			pathfinder.getPathTo(x, y).draw(WorldHolder.pipeline.buffer);
		}
	}

	@Override
	public void onClick(int button, int action, int mods, int x, int y) {
		if (world.isPositionValid(x, y) && pathfinder.canReach(x, y)) {
			world.getManager().apply(new SummonAction(pathfinder.getPathTo(x, y), (CapitolBuilding) building));
			closed = true;
		}
	}

	@Override
	public void close() {
		world.setOverlay(null);
	}

}
