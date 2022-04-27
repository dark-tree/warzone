package net.darktree.game.entites;

import net.darktree.lt2d.Registries;
import net.darktree.lt2d.graphics.vertex.Renderer;
import net.darktree.lt2d.graphics.vertex.VertexBuffer;
import net.darktree.lt2d.util.Type;
import net.darktree.lt2d.world.World;
import net.darktree.lt2d.world.entities.Entity;
import net.darktree.lt2d.world.entities.MovingEntity;

public class TestEntity extends MovingEntity {

	public TestEntity(World world, int x, int y, Type<Entity> type) {
		super(world, x, y, type);
	}

	public void draw(VertexBuffer buffer) {
		super.draw(buffer);
		Renderer.quad(buffer, x, y, 1, 1, Registries.TILE_SPRITES.get("material_ore"), 1, 1, 1, 0);
	}

}
