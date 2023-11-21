package net.darktree.warzone.world.overlay;

import net.darktree.warzone.Main;
import net.darktree.warzone.client.Colors;
import net.darktree.warzone.client.render.color.Color;
import net.darktree.warzone.world.WorldSnapshot;
import net.darktree.warzone.world.tile.TileState;

public class MapOverlay extends Overlay {

	@Override
	public Color getColor(WorldSnapshot world, int x, int y, TileState state) {
		if (state.getOwner() == world.getCurrentSymbol()) {
			return world.canControl(x, y) ? Colors.OVERLAY_NONE : ((Main.window.profiler.getFrameCount() / 30 % 2 == 0) ? Colors.OVERLAY_NONE : Colors.OVERLAY_FOREIGN);
		}

		return Colors.OVERLAY_FOREIGN;
	}

}
