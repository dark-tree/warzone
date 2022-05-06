package net.darktree.game.tiles;

import net.darktree.Main;
import net.darktree.game.buildings.Building;
import net.darktree.game.country.Symbol;
import net.darktree.lt2d.graphics.vertex.VertexBuffer;
import net.darktree.lt2d.util.Direction;
import net.darktree.lt2d.world.Tile;
import net.darktree.lt2d.world.TileInstance;
import net.darktree.lt2d.world.TileState;
import net.darktree.lt2d.world.World;
import org.jetbrains.annotations.Nullable;

/**
 * TODO: reconsider this
 *
 * Should we pass the events here or maybe call them on the buildings array in world?
 * That's not a perfect solution either so for now i will leave it as-is.
 */

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
	public boolean canPathfindOnto(World world, int x, int y) {
		return getBuilding(world, x, y).canPathfindOnto(world, x, y);
	}

	@Override
	public void onOwnerUpdate(World world, int x, int y, Symbol previous, Symbol current) {
		getBuilding(world, x, y).onOwnerUpdate(world, x, y, previous, current);
	}

	@Override
	public void onNeighbourUpdate(World world, int x, int y, Direction direction) {
		getBuilding(world, x + direction.x, y + direction.y).onNeighbourUpdate(world, x, y, direction);
	}

	@Override
	public void onPlayerTurnStart(World world, int x, int y, Symbol symbol) {
		getBuilding(world, x, y).onPlayerTurnStart(world, x, y, symbol);
	}

	@Override
	public void onPlayerTurnEnd(World world, int x, int y, Symbol symbol) {
		getBuilding(world, x, y).onPlayerTurnEnd(world, x, y, symbol);
	}

	@Override
	public void onTurnCycleEnd(World world, int x, int y) {
		getBuilding(world, x, y).onTurnCycleEnd(world, x, y);
	}

	@Override
	public void draw(int x, int y, TileState state, VertexBuffer buffer) {
		Building.Link link = Main.world.getTileInstance(x, y, Building.Link.class);

		if (link.isOrigin()) {
			link.getBuilding().draw(x, y, buffer);
		}
	}

}
