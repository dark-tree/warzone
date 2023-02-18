package net.darktree.warzone.country.ai.unit.data;

import net.darktree.warzone.world.World;
import net.darktree.warzone.world.action.MoveUnitAction;
import net.darktree.warzone.world.action.manager.Action;
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


	public int getSteps() {
		return path.getLength();
	}

	public void removeFromSearch(List<UnitSource> sources, List<UnitTarget> targets) {
		sources.remove(source);
		targets.remove(target);
	}

	public boolean isPinned() {
		return target.x == source.x && target.y == source.y;
	}

	public Action asAction(World world) {
		MoveUnitAction action = new MoveUnitAction(world, source.x, source.y);
		action.setTarget(target.x, target.y);
		return action;
	}

}
