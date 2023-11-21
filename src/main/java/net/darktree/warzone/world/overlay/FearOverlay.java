package net.darktree.warzone.world.overlay;

import net.darktree.warzone.client.Colors;
import net.darktree.warzone.client.render.color.Color;
import net.darktree.warzone.client.render.color.MutableColor;
import net.darktree.warzone.util.math.MathHelper;
import net.darktree.warzone.world.WorldSnapshot;
import net.darktree.warzone.world.terrain.ChokepointFinder;
import net.darktree.warzone.world.terrain.DangerFinder;
import net.darktree.warzone.world.tile.TileState;

public class FearOverlay extends Overlay {

	private final DangerFinder dangerFinder;
	private final ChokepointFinder tightness;

	public FearOverlay(DangerFinder dangerFinder, ChokepointFinder tightness) {
		this.dangerFinder = dangerFinder;
		this.tightness = tightness;
	}

	private Color getColor(float fear) {
		int i = MathHelper.clamp(Math.round(fear * 2f), 0, 9);
		return MutableColor.of(i / 10.0f, (10 - i) / 10.0f, 0.2f, 0.5f);
	}

	@Override
	public Color getColor(WorldSnapshot world, int x, int y, TileState state) {
		//return getColor(dangerFinder.getDerivedFear(x, y));
		int t = tightness.getTightness(x, y);
		return (t > 0) ? Colors.TEXTBOX_SELECTED : Colors.NONE;
	}

}
