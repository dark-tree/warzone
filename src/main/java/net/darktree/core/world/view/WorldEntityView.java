package net.darktree.core.world.view;

import net.darktree.core.util.Type;
import net.darktree.core.world.World;
import net.darktree.core.world.entity.Entity;

import java.util.List;

public interface WorldEntityView {

	default Entity addEntity(Entity entity) {
		getEntities().add(entity);
		return entity;
	}

	default Entity addEntity(int x, int y, Type<Entity> type) {
		return addEntity(type.construct((World) this, x, y));
	}

	default Entity getEntity(int x, int y) {
		return getEntities().stream().filter(entity -> entity.isAt(x, y)).findFirst().orElse(null);
	}

	List<Entity> getEntities();

}
