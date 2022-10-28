package net.darktree.core.world.view;

import net.darktree.core.world.World;
import net.darktree.core.world.entity.Entity;
import net.darktree.core.world.tile.TilePos;
import net.darktree.game.buildings.Building;

import java.util.List;

public interface WorldEntityView {

	default Entity addEntity(Entity entity) {
		getEntities().add(entity);
		return entity;
	}

	default Entity addEntity(int x, int y, Entity.Type type) {
		return addEntity(type.create((World) this, x, y));
	}

	default Entity getEntity(int x, int y) {
		return getEntities().stream().filter(entity -> entity.isAt(x, y)).findFirst().orElse(null);
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
