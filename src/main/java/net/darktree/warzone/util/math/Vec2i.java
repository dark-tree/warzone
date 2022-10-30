package net.darktree.warzone.util.math;

import java.util.Objects;

public class Vec2i {
	public final int x;
	public final int y;

	public Vec2i(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) return true;
		if (other instanceof Vec2i vector) {
			return vector.x == this.x && vector.y == this.y;
		}

		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y);
	}
}
