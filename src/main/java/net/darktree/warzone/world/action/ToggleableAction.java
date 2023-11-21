package net.darktree.warzone.world.action;

import net.darktree.warzone.world.action.ledger.Action;

/**
 * Extend this class when defining an actions that should fold on itself
 * (undo() the previous one, not redo() the new one) when applied in a sequence
 */
public abstract class ToggleableAction extends Action {

	public ToggleableAction(Type type) {
		super(type);
	}

	@Override
	public boolean isToggleable(Action previous) {
		return supersedes(previous);
	}

	public boolean supersedes(Action peek) {
		return this.getClass() == peek.getClass();
	}

}
