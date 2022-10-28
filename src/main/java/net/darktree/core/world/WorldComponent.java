package net.darktree.core.world;

import net.darktree.core.util.Direction;
import net.darktree.core.world.tile.variant.TileVariant;
import net.darktree.game.country.Symbol;

public interface WorldComponent extends WorldListener, WorldTile {

	/**
	 * Called right before a component is removed
	 */
	default void onRemoved(World world, int x, int y, TileVariant state) {

	}

	/**
	 * Called right after a component is added
	 */
	default void onAdded(World world, int x, int y, TileVariant state) {

	}

	/**
	 * Called when tile ownership changes
	 */
	default void onOwnerUpdate(World world, int x, int y, Symbol previous, Symbol current) {

	}

	/**
	 * Can be replaced by another tile, used by buildings
	 */
	@Deprecated
	default boolean isReplaceable() {
		return false;
	}

	/**
	 * Can be deconstructed by its owner
	 */
	default boolean isDeconstructable(World world, int x, int y) {
		return false;
	}

	/**
	 * Can be deconstructed by its owner
	 */
	default boolean isDestructible(World world, int x, int y) {
		return isDeconstructable(world, x, y);
	}

	/**
	 * Deconstruct this building, this method should trigger applicable game action
	 */
	default void deconstruct(World world, int x, int y) {

	}

	/**
	 * Can an object pass this tile in the give direction
	 */
	default boolean canPenetrate(World world, int x, int y, Direction vector) {
		return false;
	}

	default void onLoaded() {

	}
}
