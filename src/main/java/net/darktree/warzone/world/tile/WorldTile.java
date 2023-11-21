package net.darktree.warzone.world.tile;

import net.darktree.warzone.client.window.input.ClickEvent;
import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.world.WorldAccess;

public interface WorldTile {

	/**
	 * Called when a component is clicked
	 */
	default void onInteract(WorldAccess view, int x, int y, ClickEvent event) {

	}

	/**
	 * Defines whether a tile can be colonized
	 */
	default boolean canColonize(Symbol enemy) {
		return true;
	}

}
