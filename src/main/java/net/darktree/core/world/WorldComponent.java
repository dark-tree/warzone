package net.darktree.core.world;

import net.darktree.core.event.ClickEvent;
import net.darktree.core.util.Direction;
import net.darktree.core.world.tile.variant.TileVariant;
import net.darktree.game.country.Symbol;

public interface WorldComponent extends WorldListener {

	/**
	 * Defines whether a pathfinder can create a path node through this component
	 */
	boolean canPathfindThrough(World world, int x, int y);

	/**
	 * Defines whether a pathfinder can create a path to this location
	 */
	default boolean canPathfindOnto(World world, int x, int y) {
		return canPathfindThrough(world, x, y);
	}

	/**
	 * Called when a component is clicked
	 */
	void onInteract(World world, int x, int y, ClickEvent event);

	/**
	 * Called right before a component is removed
	 */
	default void onRemoved(World world, int x, int y, TileVariant state) {

	}

	/**
	 * Called when tile ownership changes
	 */
	default void onOwnerUpdate(World world, int x, int y, Symbol previous, Symbol current) {

	}

	/**
	 * Called when a neighbour tile changes variants, x & y point to the neighbour
	 */
	@Deprecated(forRemoval = true)
	default void onNeighbourUpdate(World world, int x, int y, Direction direction) {

	}

	/**
	 * Defines whether a tile can be colonized
	 */
	default boolean canColonize(World world, int x, int y) {
		return true;
	}

	/**
	 * Can be replaced by another tile, used by buildings
	 */
	default boolean isReplaceable() {
		return false;
	}

}
