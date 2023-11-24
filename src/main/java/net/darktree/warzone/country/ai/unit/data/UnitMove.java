package net.darktree.warzone.country.ai.unit.data;

import net.darktree.warzone.world.action.MoveUnitAction;
import net.darktree.warzone.world.action.ledger.Action;
import net.darktree.warzone.world.path.Path;

import java.util.List;

public final class UnitMove {

	public final Path path;
	public final UnitTarget target;
	public final UnitSource source;

	public UnitMove(UnitTarget target, UnitSource source) {
		this(target, source, source.finder.getPathTo(target.x, target.y));
	}

	public UnitMove(UnitTarget target, UnitSource source, Path path) {
		this.target = target;
		this.source = source;
		this.path = path;
	}

	/**
	 * Get the total number of steps between source and target
	 */
	public int getSteps() {
		return path.getLength();
	}

	/**
	 * Remove stored source and target from given lists
	 */
	public void removeFromSearch(List<UnitSource> sources, List<UnitTarget> targets) {
		sources.remove(source);
		targets.remove(target);
	}

	/**
	 * Check if this move is a no-op (source is on the target)
	 */
	public boolean isPinned() {
		return target.x == source.x && target.y == source.y;
	}

	/**
	 * Create an action that matches this move
	 */
	public Action asAction() {
		return new MoveUnitAction(source.x, source.y, target.x, target.y);
	}

}
