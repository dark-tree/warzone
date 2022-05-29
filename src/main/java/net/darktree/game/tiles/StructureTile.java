package net.darktree.game.tiles;

import net.darktree.core.client.render.vertex.VertexBuffer;
import net.darktree.core.event.ClickEvent;
import net.darktree.core.event.TurnEvent;
import net.darktree.core.world.World;
import net.darktree.core.world.tile.Tile;
import net.darktree.core.world.tile.TileInstance;
import net.darktree.core.world.tile.TileState;
import net.darktree.game.buildings.Building;
import net.darktree.game.country.Symbol;
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
	public void onInteract(World world, int x, int y, ClickEvent event) {
		getBuilding(world, x, y).onInteract(world, x, y, event);
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
	public void onPlayerTurnEvent(World world, int x, int y, TurnEvent event, Symbol symbol) {
		getBuilding(world, x, y).onPlayerTurnEvent(world, x, y, event, symbol);
	}

	@Override
	public boolean isReplaceable() {
		return false;
	}

	@Override
	public void draw(World world, int x, int y, TileState state, VertexBuffer buffer) {
		Building.Link link = world.getTileInstance(x, y, Building.Link.class);

		if (link.isOrigin()) {
			link.getBuilding().draw(x, y, buffer);
		}
	}

}
