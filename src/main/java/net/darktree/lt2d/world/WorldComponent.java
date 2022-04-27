package net.darktree.lt2d.world;

import net.darktree.lt2d.world.state.TileVariant;

public interface WorldComponent {

	/**
	 * Defines whether a pathfinder can create a path node through this component
	 */
	boolean canPathfindThrough(World world, int x, int y);

	/**
	 * Called when a component is clicked
	 */
	void onInteract(World world, int x, int y, int mode);

	/**
	 * Called when a component is removed
	 */
	void onRemoved(World world, int x, int y, TileVariant state);

}
