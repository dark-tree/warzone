package net.darktree.warzone.country.ai;

import net.darktree.warzone.world.tile.TilePos;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class WeighedPos extends TilePos implements Comparable<WeighedPos> {

	public final int weight;

	public WeighedPos(int x, int y, int weight) {
		super(x, y);
		this.weight = weight;
	}

	/**
	 * Create a new WeighedPos from the given position and weight
	 */
	public static WeighedPos wrap(TilePos pos, int weight) {
		return new WeighedPos(pos.x, pos.y, weight);
	}

	/**
	 * Picks the WeighedPos of the highest weight from the given list
	 */
	public static WeighedPos highest(List<WeighedPos> list) {
		WeighedPos pos = null;

		for (WeighedPos entry : list) {
			if (pos == null || entry.weight > pos.weight) {
				pos = entry;
			}
		}

		return pos;
	}

	/**
	 * Create a new WeighedPos of the same weight but with a different position
	 */
	public WeighedPos childPos(TilePos pos) {
		return new WeighedPos(pos.x, pos.y, weight);
	}

	@Override
	public int compareTo(@NotNull WeighedPos other) {
		return Integer.compare(weight, other.weight);
	}

	@Override
	public String toString() {
		return x + " " + y + " w=" + weight;
	}
}
