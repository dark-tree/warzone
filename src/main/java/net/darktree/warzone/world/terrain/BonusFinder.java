package net.darktree.warzone.world.terrain;

import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.world.World;
import net.darktree.warzone.world.pattern.Patterns;
import net.darktree.warzone.world.tile.TilePos;

import java.util.ArrayList;
import java.util.List;

public class BonusFinder extends AbstractFinder {

	private final List<Bonus> bonuses = new ArrayList<>();
	private final Symbol symbol;

	public BonusFinder(World world, Symbol symbol) {
		super(Patterns.NEIGHBOURS, world);
		this.symbol = symbol;

		compute();
	}

	public int grant() {
		final int count = bonuses.size();
		bonuses.forEach(bonus -> bonus.transfer(world));
		bonuses.clear();
		return count;
	}

	protected void compute() {
		for (int x = 0; x < width; x ++) {
			for (int y = 0; y < height; y ++) {
				if (checkNeighbours(x, y)) {
					bonuses.add(new Bonus(x, y, this.symbol));
				}
			}
		}
	}

	private boolean checkNeighbours(int x, int y) {
		if (!getTile(x, y).canColonize(this.symbol)) {
			return false;
		}

		for (TilePos offset : offsets) {
			if (!checkTile(x + offset.x, y + offset.y)) return false;
		}

		return true;
	}

	private boolean checkTile(int x, int y) {
		return !isPosValid(x, y) || !getTile(x, y).canColonize(this.symbol) || getOwner(x, y) == this.symbol;
	}

}
