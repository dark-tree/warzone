package net.darktree.warzone.country.ai.unit.data;

import java.util.LinkedList;

public final class UnitSolution {
	public final LinkedList<UnitMove> moves = new LinkedList<>();
	public final LinkedList<UnitTarget> unreachable = new LinkedList<>();
}
