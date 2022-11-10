package net.darktree.warzone.world.action.manager;

import com.google.common.collect.ImmutableMap;
import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.util.Logger;
import net.darktree.warzone.util.Util;
import net.darktree.warzone.world.World;
import net.querz.nbt.tag.CompoundTag;

public abstract class ActionManager {

	private final World world;
	private final ImmutableMap<Symbol, ActionQueue> queues = Util.enumMapOf(Symbol.class, ActionQueue::new);

	public ActionManager(World world) {
		this.world = world;
	}

	private ActionQueue get(Symbol symbol) {
		return queues.get(symbol);
	}

	public final void clear(Symbol symbol) {
		get(symbol).clear();
	}

	public boolean applyReceived(Symbol symbol, CompoundTag nbt) {
		return apply(symbol, Action.fromNbt(nbt, world));
	}

	public boolean apply(Symbol symbol, Action action) {
		return get(symbol).push(action);
	}

	public boolean undo(Symbol symbol) {
		return get(symbol).pop();
	}

	@Deprecated
	public final boolean apply(Action action) {
		return apply(world.getCurrentSymbol(), action);
	}

	public static class Client extends ActionManager {

		public Client(World world) {
			super(world);
		}

		@Override
		public boolean apply(Symbol symbol, Action action) {
			if (action instanceof HostAction) {
				Logger.warn("Host action invoked on the client! Aborted!");
				return false;
			}

			return false; // TODO send action packet to host if valid
		}

		@Override
		public boolean undo(Symbol symbol) {
			return false; // TODO send undo packet to host if valid
		}

	}

	public static class Host extends ActionManager {

		public Host(World world) {
			super(world);
		}

		@Override
		public boolean applyReceived(Symbol symbol, CompoundTag nbt) {
			throw new AssertionError("Operation not supported on host!");
		}

	}

}
