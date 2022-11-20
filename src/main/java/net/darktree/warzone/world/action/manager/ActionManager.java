package net.darktree.warzone.world.action.manager;

import com.google.common.collect.ImmutableMap;
import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.network.Packets;
import net.darktree.warzone.network.UserGroup;
import net.darktree.warzone.util.Logger;
import net.darktree.warzone.util.Util;
import net.darktree.warzone.world.World;

public class ActionManager {

	protected final World world;
	protected final ImmutableMap<Symbol, ActionQueue> queues = Util.enumMapOf(Symbol.class, ActionQueue::new);

	public ActionManager(World world) {
		this.world = world;
	}

	protected ActionQueue get(Symbol symbol) {
		return queues.get(symbol);
	}

	public final void clear(Symbol symbol) {
		get(symbol).clear();
	}

	public boolean apply(Symbol symbol, Action action, boolean received) {
		return get(symbol).push(action);
	}

	public boolean undo(Symbol symbol, boolean received) {
		return get(symbol).pop();
	}

	@Deprecated
	public final boolean apply(Action action) {
		return apply(world.getCurrentSymbol(), action, false);
	}

	@Deprecated
	public final boolean undo() {
		return undo(world.getCurrentSymbol(), false);
	}

	public static class Client extends ActionManager {

		public Client(World world) {
			super(world);
		}

		@Override
		public boolean apply(Symbol symbol, Action action, boolean received) {
			if (received) {
				return get(symbol).push(action);
			}

			if (action instanceof HostAction) {
				Logger.warn("Host action invoked on the client! Aborted!");
				return false;
			}

			if (action.verify(symbol)) {
				Packets.ACTION.of(symbol, action).sendToHost(UserGroup.instance);
				return true;
			}

			return false;
		}

		@Override
		public boolean undo(Symbol symbol, boolean received) {
			if (received) {
				return get(symbol).pop();
			}

			if (get(symbol).canPop()) {
				Packets.UNDO.of(symbol).sendToHost(UserGroup.instance);
				return true;
			}

			return false;
		}

	}

	public static class Host extends ActionManager {

		public Host(World world) {
			super(world);
		}

		@Override
		public boolean apply(Symbol symbol, Action action, boolean received) {
			if (super.apply(symbol, action, received)) {
				Packets.ACTION.of(symbol, action).broadcast(UserGroup.instance);
				return true;
			}

			return false;
		}

		@Override
		public boolean undo(Symbol symbol, boolean received) {
			if (super.undo(symbol, received)) {
				Packets.UNDO.of(symbol).broadcast(UserGroup.instance);
				return true;
			}

			return false;
		}
	}

}
