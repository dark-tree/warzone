package net.darktree.warzone.util.math;

import net.darktree.warzone.world.tile.TilePos;
import net.querz.nbt.tag.CompoundTag;

public class Vec2i {

	public final int x;
	public final int y;

	public Vec2i(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Serialize to Nbt
	 */
	public void toNbt(CompoundTag nbt) {
		nbt.putInt("x", x);
		nbt.putInt("y", y);
	}

	/**
	 * Convert to a TilePos instance
	 */
	public TilePos asTilePos() {
		return new TilePos(x, y);
	}

	/**
	 * Check if the position is equal
	 */
	public boolean equals(int x, int y) {
		return this.x == x && this.y == y;
	}

	/**
	 * Get the manhattan distance to another Vec2i
	 */
	public int manhattan(Vec2i other) {
		return MathHelper.getManhattanDistance(x, y, other.x, other.y);
	}

	@Override
	public String toString() {
		return x + " " + y;
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
		return 31 * x + y;
	}

}
