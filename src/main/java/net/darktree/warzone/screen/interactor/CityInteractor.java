package net.darktree.warzone.screen.interactor;

import net.darktree.warzone.Main;
import net.darktree.warzone.client.render.vertex.VertexBuffer;
import net.darktree.warzone.client.window.Input;
import net.darktree.warzone.client.window.input.ClickEvent;
import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.world.WorldAccess;
import net.darktree.warzone.world.WorldSnapshot;
import net.darktree.warzone.world.action.SummonAction;
import net.darktree.warzone.world.entity.building.Building;
import net.darktree.warzone.world.overlay.PathFinderOverlay;

public class CityInteractor extends Interactor {

	private final SummonAction action;
	private final WorldAccess world;

	public CityInteractor(Symbol symbol, WorldAccess world) {
		WorldSnapshot snapshot = world.getTrackingWorld();
		Building building = snapshot.getCountry(symbol).getCapitol();
		this.action = new SummonAction(building.getX(), building.getY());
		this.world = world;

		world.getView().setOverlay(new PathFinderOverlay(action.getPathfinder()));
	}

	@Override
	public void draw(VertexBuffer texture, VertexBuffer color) {
		final Input input = Main.window.input();
		final int x = input.getMouseTileX(world.getView());
		final int y = input.getMouseTileY(world.getView());

		if (world.isPositionValid(x, y) && action.getPathfinder().canReach(x, y)) {
			action.getPathfinder().getPathTo(x, y).draw(color);
		}
	}

	@Override
	public void onClick(ClickEvent event, int x, int y) {
		if (world.isPositionValid(x, y)) {
			if (this.action.setTarget(x, y)) {
				world.getLedger().push(this.action);
				closed = true;
			}
		}
	}

	@Override
	public void close() {
		world.getView().hideOverlay();
	}

}
