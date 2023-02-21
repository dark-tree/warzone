package net.darktree.warzone.util.iterable;

import net.darktree.warzone.util.math.Vec2i;

public final class SpiralPoint extends Vec2i {

	public final int r;

	SpiralPoint(int r, int x, int y) {
		super(x, y);
		this.r = r;
	}

	@Override
	public String toString() {
		return "SpiralPoint{x=" + x + ", y=" + y + ", r=" + r + "}";
	}

}
