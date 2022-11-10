package net.darktree.warzone.world.action;

import net.darktree.warzone.world.World;
import net.darktree.warzone.world.action.manager.Action;

/**
 * Extend this class when defining an actions that should fold on itself
 * (undo() the previous one, not redo() the new one) when applied in a sequence
 */
public abstract class ToggleableAction extends Action {

	public ToggleableAction(World world, Type type) {
		super(world, type);
	}

	public boolean supersedes(ToggleableAction peek) {
		return this.getClass() == peek.getClass();
	}

}
