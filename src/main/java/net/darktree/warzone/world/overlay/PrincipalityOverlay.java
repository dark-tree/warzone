package net.darktree.warzone.world.overlay;

import net.darktree.warzone.client.Colors;
import net.darktree.warzone.client.render.color.Color;
import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.world.World;
import net.darktree.warzone.world.terrain.ColonizationFinder;
import net.darktree.warzone.world.terrain.PrincipalityRangeFinder;
import net.darktree.warzone.world.terrain.TargetFinder;
import net.darktree.warzone.world.tile.TilePos;
import net.darktree.warzone.world.tile.TileState;

public class PrincipalityOverlay extends Overlay {

	private final static int R = 6;

	private final PrincipalityRangeFinder radius;
	private final TargetFinder target;
	private final ColonizationFinder colonization;

	private final Color[] colors = new Color[R];

	public PrincipalityOverlay(World world) {
		this.radius = new PrincipalityRangeFinder(world);
		this.target = new TargetFinder(Symbol.CROSS, radius);
		this.colonization = new ColonizationFinder(Symbol.CROSS, world, target.getTargets(), true);

		for (int i = 0; i < R; i ++) {
			colors[i] = new Color(i / (float) R, ((float) R - i) / (float) R, 0.2f, 0.5f);
		}
	}

	private int getTargetIndex(int x, int y) {
		return target.getTargets().indexOf(new TilePos(x, y));
	}

	@Override
	public Color getColor(World world, int x, int y, TileState state) {
		int i = getTargetIndex(x, y);

		if (i != -1) {
			return new Color(0.9f, 1 - (i / (float) target.getTargets().size()), 0.1f, 1f);
		}

		if (radius.getPrincipality(x, y) != Symbol.CROSS) {
			return Colors.NONE;
		}

		return colors[(R - 1) - Math.min(radius.getPrincipalityRange(x, y), R - 1)];
	}

}
