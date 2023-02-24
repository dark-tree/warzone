package net.darktree.warzone.world;

import net.darktree.warzone.world.tile.TilePos;

import java.util.List;

public interface Warp {

	/**
	 * Returns a list af all the tiles connected (linked) with this warp.
	 * A unit must be able to re-enter the warp while standing on any of them,
	 * otherwise the PathFinder will fail during back propagation.
	 *
	 * @return a list of linked tiles, {@link Warp#canWarpFrom(int, int)} must return true for all of them
	 */
	List<TilePos> getWarpedTiles();

	/**
	 * Returns if this warp is "direct". This controls weather bullets
	 * and colonization should be able to cross the warp onto the other side.
	 * This <b>must only</b> return true if this warp has a one-to-one connection (exact two lined tiles)
	 */
	boolean isWarpDirect();

	/**
	 * Check if entrance into the warp is valid from the given position,
	 * must return true for all positions returned by {@link Warp#getWarpedTiles()}
	 *
	 * @return if a <b>thing</b> located on given x, y can teleport to any of the linked tiles
	 */
	boolean canWarpFrom(int x, int y);

}
