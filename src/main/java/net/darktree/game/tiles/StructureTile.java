package net.darktree.game.tiles;

import net.darktree.Main;
import net.darktree.game.buildings.Building;
import net.darktree.lt2d.graphics.vertex.VertexBuffer;
import net.darktree.lt2d.world.Tile;
import net.darktree.lt2d.world.TileInstance;
import net.darktree.lt2d.world.TileState;
import net.darktree.lt2d.world.World;
import org.jetbrains.annotations.Nullable;

public class StructureTile extends Tile {

	@Override
	public @Nullable TileInstance getInstance(World world, int x, int y) {
		return new Building.Link(world, x, y);
	}

	/**
	 * Get the Building object associated with this structure tile, by querying
	 * it from the Building Link Tile Instance.
	 */
	protected Building getBuilding(World world, int x, int y) {
		return world.getTileInstance(x, y, Building.Link.class).getBuilding();
	}

	@Override
	public void onInteract(World world, int x, int y, int mode) {
		getBuilding(world, x, y).onInteract(world, x, y, mode);
	}

	@Override
	public boolean canPathfindThrough(World world, int x, int y) {
		return getBuilding(world, x, y).canPathfindThrough(world, x, y);
	}

	@Override
	public void draw(int x, int y, TileState state, VertexBuffer buffer) {
		getBuilding(Main.world, x, y).draw(x, y, buffer);
	}
}
