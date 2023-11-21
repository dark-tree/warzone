package net.darktree.warzone.world.overlay;

import net.darktree.warzone.client.Colors;
import net.darktree.warzone.client.render.color.Color;
import net.darktree.warzone.world.WorldSnapshot;
import net.darktree.warzone.world.path.PathFinder;
import net.darktree.warzone.world.tile.TileState;

public class PathFinderOverlay extends Overlay {

	private final PathFinder pathfinder;

	public PathFinderOverlay(PathFinder pathfinder) {
		this.pathfinder = pathfinder;
	}

	@Override
	public Color getColor(WorldSnapshot world, int x, int y, TileState state) {
		return pathfinder.canReach(x, y) ? Colors.OVERLAY_REACHABLE : Colors.OVERLAY_NONE;
	}

	@Override
	public void markDirty() {
		pathfinder.update();
	}

}
