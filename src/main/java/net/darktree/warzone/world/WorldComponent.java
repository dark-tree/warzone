package net.darktree.warzone.world;

import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.util.Direction;
import net.darktree.warzone.world.tile.WorldTile;

public interface WorldComponent extends WorldListener, WorldTile {

	/**
	 * Called right before a component is removed,
	 * This method should manually update entity cache.
	 */
	default void onRemoved() {

	}

	/**
	 * Called right after a component is added to the entity list,
	 * This method should manually update entity cache.
	 */
	default void onAdded() {

	}

	/**
	 * Called right before a component is added to the world during loading from NBT
	 */
	default void onLoaded() {

	}

	/**
	 * Called when tile ownership changes
	 */
	default void onOwnerUpdate(Symbol previous, Symbol current) {

	}

	/**
	 * Can be deconstructed by its owner
	 */
	default boolean isDeconstructable() {
		return false;
	}

	/**
	 * Can be destroyed
	 */
	default boolean isDestructible() {
		return isDeconstructable();
	}

	/**
	 * Deconstruct this building, this method should trigger applicable game action
	 */
	default void deconstruct() {

	}

	/**
	 * Can an object pass this tile in the give direction
	 */
	default boolean canPenetrate(Direction vector) {
		return false;
	}

	/**
	 * Returns if a different world component of the given symbol pass though this one
	 */
	default boolean canPathfindThrough(Symbol symbol) {
		return false;
	}

}
