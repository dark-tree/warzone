package net.darktree.warzone.screen.interactor;

import net.darktree.warzone.Main;
import net.darktree.warzone.client.render.vertex.VertexBuffer;
import net.darktree.warzone.client.window.Input;
import net.darktree.warzone.client.window.input.ClickEvent;
import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.world.WorldAccess;
import net.darktree.warzone.world.WorldSnapshot;
import net.darktree.warzone.world.action.SummonAction;
import net.darktree.warzone.world.entity.building.CapitolBuilding;
import net.darktree.warzone.world.overlay.PathFinderOverlay;
import net.darktree.warzone.world.path.PathFinder;

public class CityInteractor extends Interactor {

	private final CapitolBuilding building;
	private final PathFinder pathfinder;
	private final WorldAccess world;

	public CityInteractor(Symbol symbol, WorldAccess world) {
		WorldSnapshot snapshot = world.getTrackingWorld();

		this.building = snapshot.getCountry(symbol).getCapitol();
		this.world = world;
		this.pathfinder = building.getPathFinder();

		world.getView().setOverlay(new PathFinderOverlay(pathfinder));
	}

	@Override
	public void draw(VertexBuffer texture, VertexBuffer color) {
		final Input input = Main.window.input();
		final int x = input.getMouseTileX(world.getView());
		final int y = input.getMouseTileY(world.getView());

		if (!closed && world.isPositionValid(x, y) && pathfinder.canReach(x, y)) {
			pathfinder.getPathTo(x, y).draw(color);
		}
	}

	@Override
	public void onClick(ClickEvent event, int x, int y) {
		if (!closed && world.isPositionValid(x, y) && pathfinder.canReach(x, y)) {
			world.getLedger().push(new SummonAction(building.getX(), building.getY(), x, y));
			closed = true;
		}
	}

	@Override
	public void close() {
		world.getView().hideOverlay();
	}

}
