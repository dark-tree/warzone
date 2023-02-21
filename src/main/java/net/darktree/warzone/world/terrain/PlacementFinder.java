package net.darktree.warzone.world.terrain;

import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.country.ai.WeighedPos;
import net.darktree.warzone.world.World;
import net.darktree.warzone.world.pattern.Patterns;
import net.darktree.warzone.world.tile.TilePos;

import java.util.ArrayList;
import java.util.List;

public class PlacementFinder extends AbstractFinder {

	private static final float GAIN_FACTOR = 0.33f;
	private static final float FEAR_FACTOR = 0.25f;

	private final Symbol symbol;
	private final DangerFinder danger;

	public PlacementFinder(World world, Symbol symbol, DangerFinder danger) {
		super(Patterns.NEIGHBOURS, world);
		this.symbol = symbol;
		this.danger = danger;
	}

	public List<WeighedPos> solve() {
		List<WeighedPos> targets = new ArrayList<>();

		world.getBorder().getBorderTiles(symbol).forEach(pos -> {
			targets.add(WeighedPos.wrap(pos, getWeight(pos)));
		});

		return targets;
	}

	private int getWeight(TilePos pos) {
		float gain = getGain(pos.x, pos.y) * GAIN_FACTOR;
		float fear = getNeighbourCount(pos.x, pos.y) * FEAR_FACTOR;
		float sum = gain + fear;

		if (sum > 0 && sum < 1) {
			return 1;
		}

		return Math.round(sum);
	}

	private int getNeighbourCount(int x, int y) {
		int neighbours = 0;

		for (TilePos offset : Patterns.RING.getOffsets()) {
			int tx = offset.x + x;
			int ty = offset.y + y;

			if (isPosValid(tx, ty)) {
				Symbol owner = getOwner(tx, ty);

				if (owner != Symbol.NONE && owner != symbol) {
					neighbours++;
				}
			}
		}

		return neighbours;
	}

}
