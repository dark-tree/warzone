package net.darktree.warzone.world;

import net.darktree.warzone.world.entity.Entity;
import net.darktree.warzone.world.entity.building.Building;
import net.darktree.warzone.world.tile.TilePos;

import java.util.List;
import java.util.Optional;

public interface WorldEntityView {

	default Entity addEntity(Entity entity) {
		getEntities().add(entity);
		entity.onAdded();
		return entity;
	}

	default Entity addEntity(Entity.Type type, int x, int y) {
		return addEntity(type.create((World) this, x, y));
	}

	Entity getEntity(int x, int y);

	@SuppressWarnings("unchecked")
	default <T extends Entity> T getEntity(int x, int y, Class<T> clazz) {
		Entity entity = getEntity(x, y);
		return clazz.isInstance(entity) ? (T) entity : null;
	}

	@Deprecated
	default Optional<Entity> getOptionalEntity(int x, int y) {
		return Optional.ofNullable(getEntity(x, y));
	}

	@Deprecated
	default Entity getEntity(TilePos pos) {
		return getEntity(pos.x, pos.y);
	}

	List<Entity> getEntities();

	@Deprecated
	default Building getBuilding(int x, int y){
		Entity entity = getEntity(x, y);

		if (entity instanceof Building building) {
			return building;
		}

		return null;
	}

}
