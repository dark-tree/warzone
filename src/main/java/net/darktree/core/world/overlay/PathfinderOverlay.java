package net.darktree.core.world.overlay;

import net.darktree.core.client.Colors;
import net.darktree.core.client.render.color.Color;
import net.darktree.core.world.World;
import net.darktree.core.world.path.Pathfinder;
import net.darktree.core.world.tile.TileState;

public class PathfinderOverlay implements Overlay {

	private final Pathfinder pathfinder;

	public PathfinderOverlay(Pathfinder pathfinder) {
		this.pathfinder = pathfinder;
	}

	@Override
	public Color getColor(World world, int x, int y, TileState state) {
		return pathfinder.canReach(x, y) ? Colors.OVERLAY_REACHABLE : Colors.OVERLAY_NONE;
	}

}
