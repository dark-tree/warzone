package net.darktree.core.world;

import net.darktree.core.event.ClickEvent;

public interface WorldTile {

	/**
	 * Called when a component is clicked
	 */
	default void onInteract(World world, int x, int y, ClickEvent event) {

	}

	/**
	 * Defines whether a tile can be colonized
	 */
	default boolean canColonize(World world, int x, int y) {
		return true;
	}

}
