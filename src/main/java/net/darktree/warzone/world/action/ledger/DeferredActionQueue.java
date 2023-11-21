package net.darktree.warzone.world.action.ledger;

import net.darktree.warzone.Main;
import net.darktree.warzone.util.Util;
import net.darktree.warzone.world.WorldLedger;

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

		public DeferredActionQueue bake(int delay, Runnable terminator) {
			return new DeferredActionQueue(delay, actions, terminator);
		}

	}

	private final int delay;
	private final Queue<Action> actions;
	private final Runnable terminator;

	private DeferredActionQueue(int delay, Queue<Action> actions, Runnable terminator) {
		this.delay = delay;
		this.actions = actions;
		this.terminator = terminator;
	}

	public static Recorder record() {
		return new Recorder();
	}

	public void replay(WorldLedger ledger) {
		if (actions.isEmpty()) {
			terminator.run();
			return;
		}

		Util.runAsyncAfter(() -> Main.runSynced(() -> {
			ledger.push(actions.poll());
			replay(ledger);
		}), delay);
	}

}
