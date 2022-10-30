package net.darktree.warzone.world.action;

/**
 * Extend this class when defining an actions that should fold on itself
 * (undo() the previous one, not redo() the new one) when applied in a sequence
 */
public abstract class ToggleableAction extends Action {

	public boolean supersedes(ToggleableAction peek) {
		return this.getClass() == peek.getClass();
	}

}
