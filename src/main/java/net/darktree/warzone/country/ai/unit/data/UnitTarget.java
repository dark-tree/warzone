package net.darktree.warzone.country.ai.unit.data;

import net.darktree.warzone.world.tile.TilePos;

import java.util.HashSet;
import java.util.Set;

public final class UnitTarget extends TilePos implements Comparable<UnitTarget> {

	public final int weight;
	public final Set<UnitSource> candidates;

	public UnitTarget(int x, int y, int weight) {
		super(x, y);
		this.weight = weight;
		this.candidates = new HashSet<>();
	}

	@Override
	public int compareTo(UnitTarget other) {
		return Integer.compare(weight, other.weight);
	}

	public boolean isUnreachable() {
		return candidates.isEmpty();
	}

//	public Boolean isOccupied(World world) {
//		return world.getEntity(x, y) != null;
//	}
}
