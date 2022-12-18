package net.darktree.warzone.world;

import net.darktree.warzone.world.entity.Entity;
import net.darktree.warzone.world.tile.TilePos;

import javax.annotation.Nullable;
import java.util.List;

public interface WorldEntityView {

	/**
	 * Get the entity at the given pos
	 * @return found entity or null if there was no entity
	 */
	@Nullable Entity getEntity(int x, int y);

	/**
	 * Get the list of all entities in the world, used for rendering
	 * @return list of all entities
	 */
	List<Entity> getEntities();

	/**
	 * Adds a new entity into the world, and calls the
	 * {@link Entity#onAdded()} callback
	 * @return the added entity
	 */
	default Entity addEntity(Entity entity) {
		getEntities().add(entity);
		entity.onAdded();
		return entity;
	}

	/**
	 * Creates a new entity of the given type at the given pos, and calls the
	 * {@link Entity#onAdded()} callback
	 * @return the added entity
	 */
	default Entity addEntity(Entity.Type type, int x, int y) {
		return addEntity(type.create((World) this, x, y));
	}

	/**
	 * Get the entity at the given pos of the given type
	 * @return found entity or null if there was no entity, or it was of a different type
	 */
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
