package net.darktree.warzone.world.overlay;

import net.darktree.warzone.client.Colors;
import net.darktree.warzone.client.render.color.Color;
import net.darktree.warzone.client.render.color.MutableColor;
import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.world.WorldSnapshot;
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

	public PrincipalityOverlay(WorldSnapshot world) {
		this.radius = new PrincipalityRangeFinder(world);
		this.target = new TargetFinder(Symbol.CROSS, radius);
		this.colonization = new ColonizationFinder(Symbol.CROSS, world, target.getTargets(), true);
	}

	private int getTargetIndex(int x, int y) {
		return target.getTargets().indexOf(new TilePos(x, y));
	}

	@Override
	public Color getColor(WorldSnapshot world, int x, int y, TileState state) {
		int i = getTargetIndex(x, y);

		if (i != -1) {
			return MutableColor.of(0.9f, 1 - (i / (float) target.getTargets().size()), 0.1f, 1f);
		}

		if (radius.getPrincipality(x, y) != Symbol.CROSS) {
			return Colors.NONE;
		}

		int r = (R - 1) - Math.min(radius.getPrincipalityRange(x, y), R - 1);
		return MutableColor.of(r / (float) R, ((float) R - r) / (float) R, 0.2f, 0.5f);
	}

}
