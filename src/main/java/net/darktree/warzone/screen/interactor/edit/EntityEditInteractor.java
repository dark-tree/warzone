package net.darktree.warzone.screen.interactor.edit;

import net.darktree.warzone.client.render.image.Sprite;
import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.world.WorldAccess;
import net.darktree.warzone.world.WorldSnapshot;
import net.darktree.warzone.world.entity.Entity;
import net.darktree.warzone.world.entity.building.Building;
import net.darktree.warzone.world.tile.Surface;
import net.darktree.warzone.world.tile.Tile;
import net.darktree.warzone.world.tile.TilePos;
import net.darktree.warzone.world.tile.TileState;

import java.util.List;
import java.util.function.Consumer;

public class EntityEditInteractor extends BrushInteractor<Entity.Type> {

	private final Consumer<Entity> configurator;

	public EntityEditInteractor(Entity.Type target, Consumer<Entity> configurator, WorldAccess world) {
		super(target, world, 1);
		this.configurator = configurator;
	}

	@Override
	protected void place(Entity.Type material, int x, int y, boolean erase) {
		WorldSnapshot snapshot = world.getTrackingWorld();

		if (material == null) {
			snapshot.getEntity(x, y).remove();
		} else {
			configurator.accept(snapshot.addEntity(material, x, y));
		}
	}

	@Override
	protected Sprite sprite(Entity.Type material) {
		return material.icon;
	}

	@Override
	protected boolean verify(int x, int y) {
		WorldSnapshot snapshot = world.getTrackingWorld();

		if (!snapshot.isPositionValid(x, y)) {
			return false;
		}

		if (target == null) {
			return snapshot.getEntity(x, y) != null;
		}

		if (snapshot.getEntity(x, y) != null) {
			return false;
		}

		if (target instanceof Building.Type building) {
			Symbol symbol = snapshot.getTileState(x, y).getOwner();
			List<TilePos> tiles = building.pattern.list(snapshot, x, y, true);

			for (TilePos pos : tiles) {
				TileState state = snapshot.getTileState(pos);
				Tile tile = state.getTile();

				if (state.getEntity() != null) return false;
				if (state.getOwner() != symbol) return false;
				if (tile.getSurface() != Surface.LAND || !tile.canStayOn()) return false;
			}
		}

		return true;
	}

}
