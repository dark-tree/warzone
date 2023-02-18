package net.darktree.warzone.world.terrain;

import net.darktree.warzone.country.Country;
import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.util.math.MathHelper;
import net.darktree.warzone.world.World;
import net.darktree.warzone.world.entity.UnitEntity;
import net.darktree.warzone.world.entity.building.Building;
import net.darktree.warzone.world.pattern.Patterns;

public class DangerFinder extends AbstractFinder {

	private static final float COUNTRY_FEAR = 3.5f;
	private static final float CAPITOL_FEAR = 2.0f;
	private static final float UNIT_FEAR = 4.5f;

	private final Country country;
	private final Symbol self;
	private final float[][] direct, derived;

	public DangerFinder(Symbol self, World world) {
		super(Patterns.NEIGHBOURS, world);
		this.direct = new float[this.width][this.height];
		this.derived = new float[this.width][this.height];
		this.self = self;
		this.country = world.getCountry(self);

		compute();
	}

	public float getDirectFear(int x, int y) {
		return isPosValid(x, y) ? direct[x][y] : 0.0f;
	}

	public float getDerivedFear(int x, int y) {
		return derived[x][y];
	}

	protected void compute() {
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				this.direct[x][y] = getDirectDangerLevel(x, y);
			}
		}

		Building capitol = country.getCapitol();
		if (capitol != null) {
			capitol.forEachTile(pos -> {
				direct[pos.x][pos.y] += CAPITOL_FEAR;
			});
		}

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				this.derived[x][y] = computeDerivedFear(x, y);
			}
		}
	}

	private float computeDerivedFear(int x, int y) {
		float max = 0, min = 0;
		int r = 3;

		for (int xo = -r; xo <= r; xo ++) {
			for (int yo = -r; yo <= r; yo ++) {
				int distance = MathHelper.getManhattanDistance(0, 0, xo, yo);
				max = Math.max(max, getDirectFear(x + xo, y + yo) / (distance * distance + 1));
				min = Math.min(min, getDirectFear(x + xo, y + yo) / (distance * distance + 1));
			}
		}

		return Math.max(0, max + min);
	}

	private float getDirectDangerLevel(int x, int y) {
		float fear = 0;

		Symbol symbol = getOwner(x, y);
		if (symbol != Symbol.NONE && symbol != self) {
			fear += Math.max(-1, COUNTRY_FEAR - country.getRelationWith(symbol));
		}

		if (getEntity(x, y) instanceof UnitEntity unit) {
			symbol = unit.getSymbol();
			float boost = unit.armored ? 0.6f : 0;

			if (symbol != self) {
				fear += boost + Math.max(0, UNIT_FEAR - country.getRelationWith(symbol) / 2.0f);
			} else {
				fear -= boost + UNIT_FEAR;
			}
		}

		return fear;
	}

}
