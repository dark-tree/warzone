package net.darktree.warzone.world.action.manager;

import net.darktree.warzone.Main;
import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.util.Util;

import java.util.ArrayDeque;
import java.util.Queue;

public class DeferredActionQueue {

	public static class Recorder {

		private final Queue<Action> actions;

		private Recorder() {
			this.actions = new ArrayDeque<>();
		}

		public void push(Action action) {
			this.actions.add(action);
		}

		public DeferredActionQueue bake(Symbol symbol, int delay, Runnable terminator) {
			return new DeferredActionQueue(delay, symbol, actions, terminator);
		}

	}

	private final int delay;
	private final Symbol symbol;
	private final Queue<Action> actions;
	private final Runnable terminator;

	private DeferredActionQueue(int delay, Symbol symbol, Queue<Action> actions, Runnable terminator) {
		this.delay = delay;
		this.symbol = symbol;
		this.actions = actions;
		this.terminator = terminator;
	}

	public static Recorder record() {
		return new Recorder();
	}

	public void replay(ActionManager manager) {
		if (actions.isEmpty()) {
			terminator.run();
			return;
		}

		Util.runAsyncAfter(() -> Main.runSynced(() -> {
			manager.apply(symbol, actions.poll(), false);
			replay(manager);
		}), delay);
	}

}
