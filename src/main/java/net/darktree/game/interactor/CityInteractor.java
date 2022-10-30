package net.darktree.game.interactor;

import net.darktree.Main;
import net.darktree.core.client.render.vertex.VertexBuffer;
import net.darktree.core.client.window.Input;
import net.darktree.core.world.Surface;
import net.darktree.core.world.World;
import net.darktree.core.world.action.SummonAction;
import net.darktree.core.world.entity.building.Building;
import net.darktree.core.world.entity.building.CapitolBuilding;
import net.darktree.core.world.overlay.PathfinderOverlay;
import net.darktree.core.world.path.Pathfinder;
import net.darktree.game.country.Symbol;

public class CityInteractor extends Interactor {

	private final Pathfinder pathfinder;
	private final World world;
	private final Building building;

	public CityInteractor(Symbol symbol, World world) {
		this.building = world.getCountry(symbol).getCapitol();
		this.pathfinder = new Pathfinder(world, 10, symbol, Surface.LAND, building::forEachTile, true);
		this.world = world;

		world.setOverlay(new PathfinderOverlay(pathfinder));
	}

	@Override
	public void draw(VertexBuffer texture, VertexBuffer color) {
		final Input input = Main.window.input();
		final int x = input.getMouseMapX(world.getView());
		final int y = input.getMouseMapY(world.getView());

		if (world.isPositionValid(x, y) && pathfinder.canReach(x, y)) {
			pathfinder.getPathTo(x, y).draw(color);
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
