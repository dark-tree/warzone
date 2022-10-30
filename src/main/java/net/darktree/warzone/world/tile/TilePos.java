package net.darktree.warzone.world.tile;

import net.darktree.warzone.util.math.MathHelper;

public class TilePos {

	public final int x, y;

	public TilePos(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public String toString() {
		return x + " " + y;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;

		if (o instanceof TilePos pos) {
			return x == pos.x && y == pos.y;
		}

		return false;
	}

	@Override
	public int hashCode() {
		return 1024 * x + y;
	}

	public TilePos getMiddlePointFrom(int x, int y) {
		return MathHelper.getMiddlePoint(x, y, this.x, this.y);
	}

}
