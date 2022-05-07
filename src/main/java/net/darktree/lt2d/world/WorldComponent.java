package net.darktree.lt2d.world;

import net.darktree.game.country.Symbol;
import net.darktree.lt2d.util.Direction;
import net.darktree.lt2d.world.variant.TileVariant;

public interface WorldComponent {

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
	void onInteract(World world, int x, int y, int mode);

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
	default void onNeighbourUpdate(World world, int x, int y, Direction direction) {

	}

	/**
	 * Defines whether a tile can be colonized
	 */
	default boolean canColonize(World world, int x, int y) {
		return true;
	}

	/**
	 * Called when a turn of 'symbol' player starts
	 */
	default void onPlayerTurnStart(World world, int x, int y, Symbol symbol) {

	}

	/**
	 * Called when a turn of 'symbol' player end
	 */
	default void onPlayerTurnEnd(World world, int x, int y, Symbol symbol) {

	}

	/**
	 * Called when a turn cycle has ended
	 */
	default void onTurnCycleEnd(World world, int x, int y) {

	}

	/**
	 * Can be replaced by another tile, used by buildings
	 */
	default boolean isReplaceable() {
		return false;
	}

}
