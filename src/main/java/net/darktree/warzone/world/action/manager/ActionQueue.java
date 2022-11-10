package net.darktree.warzone.world.action.manager;

import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.world.action.ToggleableAction;

import java.util.Stack;

public class ActionQueue {

	private final Stack<Action> actions = new Stack<>();
	private final Symbol symbol;

	ActionQueue (Symbol symbol) {
		this.symbol = symbol;
	}

	boolean push(Action action) {
		if (action.verify(symbol)) {

			// if the action is toggleable remove the previous one rather than
			// adding another one if two are applied one after another
			if (action instanceof ToggleableAction toggleable) {
				if (!actions.isEmpty() && actions.peek() instanceof ToggleableAction peek) {
					if (toggleable.supersedes(peek)) return pop();
				}
			}

			action.redo(symbol);
			action.common( symbol);

			// handle actions that can't be undone
			if (action instanceof FinalAction) {
				actions.clear();
			} else {
				actions.push(action);
			}

			return true;
		}

		return false;
	}

	boolean pop() {
		if (!actions.isEmpty()) {
			Action last = actions.pop();
			last.undo(symbol);
			last.common(symbol);
			return true;
		}

		return false;
	}

	public void clear() {
		actions.clear();
	}

}
