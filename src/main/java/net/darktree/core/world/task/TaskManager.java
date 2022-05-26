package net.darktree.core.world.task;

import net.darktree.core.world.World;
import net.darktree.game.country.Symbol;

import java.util.IdentityHashMap;
import java.util.Stack;

public class TaskManager {

	private final World world;
	private final IdentityHashMap<Symbol,Stack<Task>> tasks = new IdentityHashMap<>();

	public TaskManager(World world) {
		this.world = world;

		for (Symbol symbol : Symbol.values()) {
			tasks.put(symbol, new Stack<>());
		}
	}

	public void apply(Symbol symbol, Task task) {
		task.prepare(this.world, symbol);

		if (task.verify(this.world, symbol)) {
			task.redo(this.world, symbol);
			tasks.get(symbol).push(task);
		}
	}

	public void undo(Symbol symbol) {
		Stack<Task> list = tasks.get(symbol);

		if (!list.isEmpty()) {
			list.pop().undo(this.world, symbol);
		}
	}

}
