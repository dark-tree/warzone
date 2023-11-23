package net.darktree.warzone.world;

import net.darktree.warzone.Main;
import net.darktree.warzone.country.Controller;
import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.country.controller.NullController;
import net.darktree.warzone.util.NbtSerializable;
import net.querz.nbt.tag.CompoundTag;
import org.jetbrains.annotations.NotNull;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Optional;

// TODO
public class WorldAccess implements NbtSerializable {

	private final WorldInfo info;
	private final WorldLedger ledger;
	private final WorldRenderer renderer;
	private final Map<Symbol, Controller> controllers = new IdentityHashMap<>();

	private WorldSnapshot root;
	private @Update.Flags int updates;

	public WorldAccess(WorldInfo info) {
		this.info = info;
		this.ledger = new WorldLedger(this);
		this.renderer = new WorldRenderer(this); // TODO separate from world?
	}

	public WorldInfo getInfo() {
		return info;
	}

	public WorldLedger getLedger() {
		return ledger;
	}

	public WorldSnapshot getRootWorld() {
		return root;
	}

	// TODO decouple WorldRenderer from WorldAccess
	public WorldRenderer getView() {
		return renderer;
	}

	public Symbol getCurrentSymbol() {
		return ledger.getFrame().getSymbol();
	}

	public boolean isActiveSymbol() {
		return getPlayerController(getCurrentSymbol()).isSelf();
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

	// TODO should we do it like this here?
	public Controller getPlayerController(Symbol symbol) {
		return Optional.ofNullable(controllers.get(symbol)).orElseGet(() -> {
			setPlayerController(symbol, new NullController());
			return getPlayerController(symbol);
		});
	}

	public Controller setPlayerController(Symbol symbol, Controller controller) {
		return controllers.put(symbol, controller);
	}

	void pushRenderBits(@Update.Flags int flags) {
		this.updates |= flags;
	}

	@Update.Flags int pullRenderBits() {
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
		root.toNbt(rootTag);
		nbt.put("root", rootTag);
	}

	@Override
	public void fromNbt(@NotNull CompoundTag nbt) {
		ledger.fromNbt(nbt.getCompoundTag("ledger"));
	}

	public static WorldAccess load(WorldInfo info, CompoundTag nbt) {
		WorldAccess world = new WorldAccess(info);

		// load the root world
		WorldSnapshot root = new WorldSnapshot(world, null);
		root.fromNbt(nbt.getCompoundTag("root"));
		world.root = root;

		Main.game.setWorld(world); // TODO should we do it here?
		world.fromNbt(nbt);
		return world;
	}

	public static WorldAccess createOf(WorldInfo info) {
		WorldAccess world = new WorldAccess(info);
		world.root = new WorldSnapshot(world, null);
		return world;
	}

}
