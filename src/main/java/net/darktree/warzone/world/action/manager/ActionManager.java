package net.darktree.warzone.world.action.manager;

import com.google.common.collect.ImmutableMap;
import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.network.Side;
import net.darktree.warzone.network.packet.ActionPacket;
import net.darktree.warzone.network.packet.UndoPacket;
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

	public boolean isLocal() {
		return true;
	}

	public Side getSide() {
		return Side.HOST;
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
				new ActionPacket(symbol, action).sendToHost();
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
				new UndoPacket(symbol).sendToHost();
				return true;
			}

			return false;
		}

		public boolean isLocal() {
			return false;
		}

		public Side getSide() {
			return Side.CLIENT;
		}

	}

	public static class Host extends ActionManager {

		public Host(World world) {
			super(world);
		}

		@Override
		public boolean apply(Symbol symbol, Action action, boolean received) {
			if (super.apply(symbol, action, received)) {
				new ActionPacket(symbol, action).broadcastExceptHost();
				return true;
			}

			return false;
		}

		@Override
		public boolean undo(Symbol symbol, boolean received) {
			if (super.undo(symbol, received)) {
				new UndoPacket(symbol).broadcastExceptHost();
				return true;
			}

			return false;
		}

		public boolean isLocal() {
			return false;
		}

		public Side getSide() {
			return Side.HOST;
		}

	}

}
