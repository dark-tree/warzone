package net.darktree.game.tiles;

import net.darktree.lt2d.Registries;
import net.darktree.lt2d.graphics.vertex.Renderer;
import net.darktree.lt2d.graphics.vertex.VertexBuffer;
import net.darktree.lt2d.world.Entity;
import net.darktree.lt2d.world.World;

public class TestEntity extends Entity {

	public TestEntity(World world, int x, int y, Type<?> type) {
		super(world, x, y, type);
	}

	public void draw(VertexBuffer buffer) {
		Renderer.quad(buffer, x, y, 2, 2, Registries.TILE_SPRITES.get("circle"));
	}

}
