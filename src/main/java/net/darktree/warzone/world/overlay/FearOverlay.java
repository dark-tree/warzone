package net.darktree.warzone.world.overlay;

import net.darktree.warzone.client.Colors;
import net.darktree.warzone.client.render.color.Color;
import net.darktree.warzone.util.math.MathHelper;
import net.darktree.warzone.world.World;
import net.darktree.warzone.world.terrain.ChokepointFinder;
import net.darktree.warzone.world.terrain.DangerFinder;
import net.darktree.warzone.world.tile.TileState;

public class FearOverlay extends Overlay {

	private final DangerFinder dangerFinder;
	private final ChokepointFinder tightness;
	private final Color[] colors = new Color[10];

	public FearOverlay(DangerFinder dangerFinder, ChokepointFinder tightness) {
		this.dangerFinder = dangerFinder;
		this.tightness = tightness;

		for (int i = 0; i < 10; i ++) {
			colors[i] = new Color(i / 10.0f, (10 - i) / 10.0f, 0.2f, 0.5f);
		}
	}

	private Color getColor(float fear) {
		return colors[MathHelper.clamp(Math.round(fear * 2f), 0, 9)];
	}

	@Override
	public Color getColor(World world, int x, int y, TileState state) {
		//return getColor(dangerFinder.getDerivedFear(x, y));
		int t = tightness.getTightness(x, y);
		return (t > 0) ? Colors.TEXTBOX_SELECTED : Colors.NONE;
	}

}
