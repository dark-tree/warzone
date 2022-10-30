package net.darktree.core.world;

import net.darktree.core.world.entity.Entity;
import net.darktree.core.world.entity.building.Building;
import net.darktree.core.world.tile.TilePos;

import java.util.List;
import java.util.Optional;

public interface WorldEntityView {

	default Entity addEntity(Entity entity) {
		getEntities().add(entity);
		entity.onAdded();
		return entity;
	}

	default Entity addEntity(int x, int y, Entity.Type type) {
		return addEntity(type.create((World) this, x, y));
	}

	default Entity getEntity(int x, int y) {
		return getEntities().stream().filter(entity -> entity.isAt(x, y)).findFirst().orElse(null);
	}

	default Optional<Entity> getOptionalEntity(int x, int y) {
		return Optional.ofNullable(getEntity(x, y));
	}

	default Entity getEntity(TilePos pos) {
		return getEntity(pos.x, pos.y);
	}

	List<Entity> getEntities();

	default Building getBuilding(int x, int y){
		Entity entity = getEntity(x, y);

		if (entity instanceof Building building) {
			return building;
		}

		return null;
	}

}
