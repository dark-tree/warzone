package net.darktree.game.tiles;

import net.darktree.game.buildings.Building;
import net.darktree.lt2d.Registries;
import net.darktree.lt2d.graphics.vertex.Renderer;
import net.darktree.lt2d.graphics.vertex.VertexBuffer;
import net.darktree.lt2d.util.Type;
import net.darktree.lt2d.world.World;

public class TestBuilding extends Building {

	public TestBuilding(World world, int x, int y, Type<Building> type) {
		super(world, x, y, type);
	}

	@Override
	public boolean canPathfindOnto(World world, int x, int y) {
		return false;
	}

	@Override
	public boolean canPathfindThrough(World world, int x, int y) {
		return true;
	}

	@Override
	public void draw(int x, int y, VertexBuffer buffer) {
		Renderer.quad(buffer, x, y, 2, 2, Registries.TILE_SPRITES.get("water"), 1, 1, 1, 0);
	}
}
