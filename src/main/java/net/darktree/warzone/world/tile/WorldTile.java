package net.darktree.warzone.world.tile;

import net.darktree.warzone.event.ClickEvent;
import net.darktree.warzone.world.World;

public interface WorldTile {

	/**
	 * Called when a component is clicked
	 */
	default void onInteract(World world, int x, int y, ClickEvent event) {

	}

	/**
	 * Defines whether a tile can be colonized
	 */
	default boolean canColonize() {
		return true;
	}

}
