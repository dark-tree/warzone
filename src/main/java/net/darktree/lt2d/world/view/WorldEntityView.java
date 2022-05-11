package net.darktree.lt2d.world.view;

import net.darktree.lt2d.util.Type;
import net.darktree.lt2d.world.World;
import net.darktree.lt2d.world.entities.Entity;

import java.util.List;

public interface WorldEntityView {

	default void addEntity(Entity entity) {
		getEntities().add(entity);
	}

	default void addEntity(int x, int y, Type<Entity> type) {
		getEntities().add(type.construct((World) this, x, y));
	}

	default Entity getEntity(int x, int y) {
		return getEntities().stream().filter(entity -> entity.isAt(x, y)).findFirst().orElse(null);
	}

	List<Entity> getEntities();

}
