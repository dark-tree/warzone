package net.darktree.warzone.world.overlay;

import net.darktree.warzone.client.Colors;
import net.darktree.warzone.client.render.color.Color;
import net.darktree.warzone.world.World;
import net.darktree.warzone.world.path.Pathfinder;
import net.darktree.warzone.world.tile.TileState;

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
