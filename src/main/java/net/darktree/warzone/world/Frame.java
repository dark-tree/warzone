package net.darktree.warzone.world;

import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.util.NbtSerializable;
import net.darktree.warzone.world.action.ledger.Action;
import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.ListTag;
import net.querz.nbt.tag.Tag;
import org.jetbrains.annotations.NotNull;

import java.util.Stack;
import java.util.stream.Stream;

public class Frame implements NbtSerializable {

	private final WorldLedger ledger;
	private final Symbol symbol;
	private WorldSnapshot world;

	private final Stack<Action> redo = new Stack<>();
	private final Stack<Action> undo = new Stack<>();

	Frame(WorldLedger ledger, Symbol symbol, WorldSnapshot world) {
		this.ledger = ledger;
		this.symbol = symbol;
		this.world = world;
	}

	public WorldSnapshot getWorld() {
		return world != null ? world.copy(this) : null;
	}

	public Symbol getSymbol() {
		return symbol;
	}

	public WorldLedger getLedger() {
		return ledger;
	}

	boolean push(Action action, WorldSnapshot world) {
		if (action.isToggleable(redo.peek())) {
			undo();
			return true;
		}

		if (action.apply(world, true)) {
			undo.clear();
			redo.push(action);
			return true;
		}

		return false;
	}

	boolean undo() {
		if (!redo.empty()) {
			undo.push(redo.pop());
			return true;
		}

		return false;
	}

	boolean redo(WorldSnapshot world) {
		if (!undo.empty()) {
			Action action = undo.pop();
			redo.push(action);
			action.apply(world, true);
			return true;
		}

		return false;
	}

	boolean canUndo() {
		return !redo.empty() && redo.peek().getUndoBehaviour().isRevertible();
	}

	public Stream<Action> actions() {
		return redo.stream();
	}

	public void shrink() {
		undo.clear();
	}

	@Override
	public void toNbt(@NotNull CompoundTag nbt) {
		nbt.putByte("symbol", (byte) symbol.ordinal());
		nbt.putBoolean("chained", false);

		ListTag<CompoundTag> actionsTag = new ListTag<>(CompoundTag.class);

		for (Action action : redo) {
			CompoundTag actionTag = new CompoundTag();
			action.toNbt(actionTag);
			actionsTag.add(actionTag);
		}

		nbt.put("actions", actionsTag);
	}

	@Override
	public void fromNbt(@NotNull CompoundTag nbt) {
		ListTag<?> actionsTagList = nbt.getListTag("actions");

		for (Tag<?> tag : actionsTagList) {
			redo.push(Action.load((CompoundTag) tag));
		}
	}

	public static Frame load(WorldSnapshot snapshot, WorldLedger ledger, CompoundTag nbt) {
		Symbol symbol = Symbol.values()[nbt.getByte("symbol")];
		Frame frame = new Frame(ledger, symbol, null);
		frame.world = snapshot == null ? null : snapshot.copy(frame);

		return frame;
	}

}
