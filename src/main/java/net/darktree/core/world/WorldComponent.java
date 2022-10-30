package net.darktree.core.world;

import net.darktree.core.util.Direction;
import net.darktree.game.country.Symbol;

public interface WorldComponent extends WorldListener, WorldTile {

	/**
	 * Called right before a component is removed
	 */
	default void onRemoved() {

	}

	/**
	 * Called right after a component is added
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

}
