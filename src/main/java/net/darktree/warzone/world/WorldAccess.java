package net.darktree.warzone.world;

import net.darktree.warzone.Main;
import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.util.NbtSerializable;
import net.querz.nbt.tag.CompoundTag;
import org.jetbrains.annotations.NotNull;

// TODO
public class WorldAccess implements NbtSerializable {

	private final WorldInfo info;
	private final WorldLedger ledger;
	private final WorldRenderer renderer;
	private @Update.Flags int updates;

	public WorldAccess(WorldInfo info, WorldSnapshot root) {
		this.info = info;
		this.ledger = new WorldLedger(this, root); // FIXME
		this.renderer = new WorldRenderer(this); // TODO separate from world?
	}

	public WorldInfo getInfo() {
		return info;
	}

	public WorldLedger getLedger() {
		return ledger;
	}

	// TODO decouple WorldRenderer from WorldAccess
	public WorldRenderer getView() {
		return renderer;
	}

	public Symbol getCurrentSymbol() {
		return ledger.getFrame().getSymbol();
	}

	public boolean isActiveSymbol() {
		return ledger.getTrackingWorld().getCountry(getCurrentSymbol()).getController().isSelf();
	}

	public Symbol getActiveSymbol() {
		return isActiveSymbol() ? getCurrentSymbol() : null;
	}

	public WorldSnapshot getTrackingWorld() {
		return ledger.getTrackingWorld();
	}

	public boolean isPositionValid(int x, int y) {
		return info.isPositionValid(x, y);
	}

	void pushRenderBits(@Update.Flags int flags) {
		this.updates |= flags;
	}

	// TODO restrict to package-private
	public @Update.Flags int pullRenderBits() {
		@Update.Flags int flags = this.updates;
		this.updates = 0;
		getLedger().getTrackingWorld().update(flags);
		return flags;
	}

	/**
	 * Advance the game to the next player and sends map updates
	 */
	public void nextPlayerTurn() {
//		int len = this.symbols.length;
//
//		Symbol symbol = getCurrentSymbol();
//		sendPlayerTurnEvent(TurnEvent.TURN_END, symbol);
//		manager.clear(symbol);
//
//		turn = (turn + 1) % len;
//
//		if (turn == 0) {
//			sendPlayerTurnEvent(TurnEvent.TURN_CYCLE_END, symbol);
//		}
//
//		sendPlayerTurnEvent(TurnEvent.TURN_START, getCurrentSymbol());

		ledger.beginNextFrame(info);
	}

	@Override
	public void toNbt(@NotNull CompoundTag nbt) {
		CompoundTag ledgerTag = new CompoundTag();
		ledger.toNbt(ledgerTag);
		nbt.put("ledger", ledgerTag);

		CompoundTag rootTag = new CompoundTag();
		ledger.getRootWorld().toNbt(rootTag);
		nbt.put("root", rootTag);
	}

	@Override
	public void fromNbt(@NotNull CompoundTag nbt) {
		ledger.fromNbt(nbt.getCompoundTag("ledger"));
	}

	public static WorldAccess load(WorldInfo info, CompoundTag nbt) {
		WorldSnapshot root = new WorldSnapshot(info, null); // TODO null frame?
		root.fromNbt(nbt.getCompoundTag("root"));

		WorldAccess world = new WorldAccess(info, root);
		Main.game.setWorld(world); // TODO should we do it here?
		world.fromNbt(nbt);
		return world;
	}

}
