package net.darktree.warzone.country.ai.unit.data;

import net.darktree.warzone.country.ai.WeighedPos;

import java.util.HashSet;
import java.util.Set;

public final class UnitTarget extends WeighedPos {

	public final Set<UnitSource> candidates = new HashSet<>();

	public UnitTarget(int x, int y, int weight) {
		super(x, y, weight);
	}

	public boolean isUnreachable() {
		return candidates.isEmpty();
	}

}
