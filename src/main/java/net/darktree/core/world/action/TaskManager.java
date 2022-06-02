package net.darktree.core.world.action;

import net.darktree.core.world.World;
import net.darktree.game.country.Symbol;

import java.util.IdentityHashMap;
import java.util.Stack;

public class TaskManager {

	private final World world;
	private final IdentityHashMap<Symbol,Stack<Action>> tasks = new IdentityHashMap<>();

	public TaskManager(World world) {
		this.world = world;

		for (Symbol symbol : Symbol.values()) {
			tasks.put(symbol, new Stack<>());
		}
	}

	public boolean apply(Symbol symbol, Action action) {
		if (action.verify(this.world, symbol)) {
			tasks.get(symbol).push(action);
			action.redo(this.world, symbol);
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
			list.pop().undo(this.world, symbol);
		}
	}

	public void pointOfNoReturn(Symbol symbol) {
		tasks.get(symbol).clear();
	}
}
