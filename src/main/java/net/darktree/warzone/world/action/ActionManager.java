package net.darktree.warzone.world.action;

import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.world.World;

import java.util.IdentityHashMap;
import java.util.Stack;

public class ActionManager {

	private final World world;
	private final IdentityHashMap<Symbol,Stack<Action>> tasks = new IdentityHashMap<>();

	public ActionManager(World world) {
		this.world = world;

		for (Symbol symbol : Symbol.values()) {
			tasks.put(symbol, new Stack<>());
		}
	}

	public boolean apply(Symbol symbol, Action action) {
		if (action.verify(this.world, symbol)) {
			Stack<Action> actions = tasks.get(symbol);

			if (action instanceof ToggleableAction self) {
				if (!actions.isEmpty() && actions.peek() instanceof ToggleableAction peek) {
					if (self.supersedes(peek)) {
						undo(symbol);
						return true;
					}
				}
			}

			actions.push(action);
			action.redo(this.world, symbol);
			action.common(this.world, symbol);
			return true;
		}

		return false;
	}

	public boolean apply(Action action) {
		return apply(world.getCurrentSymbol(), action);
	}

	public void undo(Symbol symbol) {
		Stack<Action> list = tasks.get(symbol);

		if (!list.isEmpty()) {
			Action action = list.pop();
			action.undo(this.world, symbol);
			action.common(this.world, symbol);
		}
	}

	public void pointOfNoReturn(Symbol symbol) {
		tasks.get(symbol).clear();
	}
}
