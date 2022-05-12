package net.darktree.core.world.overlay;

import net.darktree.core.util.Color;
import net.darktree.core.world.World;
import net.darktree.core.world.path.Pathfinder;
import net.darktree.core.world.tile.TileState;

public class PathfinderOverlay implements Overlay {

	private final Pathfinder pathfinder;

	public PathfinderOverlay(Pathfinder pathfinder) {
		this.pathfinder = pathfinder;
	}

	@Override
	public void getColor(World world, int x, int y, TileState state, Color color) {
		if (pathfinder.canReach(x, y)) {
			color.set(0.1f, 0.8f, 0.3f);
		} else {
			color.clear();
		}
	}

}
