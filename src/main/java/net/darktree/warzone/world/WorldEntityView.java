package net.darktree.warzone.world;

import net.darktree.warzone.world.entity.Entity;
import net.darktree.warzone.world.tile.TilePos;

import java.util.List;

public interface WorldEntityView {

	Entity getEntity(int x, int y);
	List<Entity> getEntities();

	default Entity addEntity(Entity entity) {
		getEntities().add(entity);
		entity.onAdded();
		return entity;
	}

	default Entity addEntity(Entity.Type type, int x, int y) {
		return addEntity(type.create((World) this, x, y));
	}

	@SuppressWarnings("unchecked")
	default <T extends Entity> T getEntity(int x, int y, Class<T> clazz) {
		Entity entity = getEntity(x, y);
		return clazz.isInstance(entity) ? (T) entity : null;
	}

	@Deprecated
	default Entity getEntity(TilePos pos) {
		return getEntity(pos.x, pos.y);
	}

}
