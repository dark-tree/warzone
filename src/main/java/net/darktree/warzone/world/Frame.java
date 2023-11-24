package net.darktree.warzone.world;

import net.darktree.warzone.util.NbtSerializable;
import net.darktree.warzone.util.TriState;
import net.darktree.warzone.world.action.ledger.Action;
import net.darktree.warzone.world.action.ledger.UndoBehaviour;
import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.ListTag;
import net.querz.nbt.tag.Tag;
import org.jetbrains.annotations.NotNull;

import java.util.Stack;
import java.util.stream.Stream;

public class Frame implements NbtSerializable {

	private final WorldLedger ledger;
	private final WorldSnapshot world;
	private final Stack<Action> redo = new Stack<>();
	private final Stack<Action> undo = new Stack<>();

	Frame(WorldLedger ledger, WorldSnapshot snapshot) {
		this.ledger = ledger;
		this.world = snapshot;
	}

	WorldSnapshot getWorld() {
		return world != null ? world.copy() : null;
	}

	WorldLedger getLedger() {
		return ledger;
	}

	boolean push(Action action, WorldSnapshot world) {
		if (!redo.empty() && action.isToggleable(redo.peek())) {
			return undo(world).orDefaultTo(true);
		}

		if (action.redo(world, true)) {
			undo.clear();
			redo.push(action);
			return true;
		}

		return false;
	}

	TriState undo(WorldSnapshot world) {
		if (!redo.empty()) {

			Action action = redo.peek();
			if (action.undo(world)) {
				undo.push(action);
				redo.pop();

				return action.getUndoBehaviour() == UndoBehaviour.REPLAY_ACTIONS ? TriState.TRUE : TriState.DEFAULT;
			}
		}

		return TriState.FALSE;
	}

	boolean redo(WorldSnapshot world) {
		if (!undo.empty()) {
			Action action = undo.pop();
			redo.push(action);
			action.redo(world, true);
			return true;
		}

		return false;
	}

	boolean canUndo() {
		return !redo.empty() && redo.peek().getUndoBehaviour().isRevertible();
	}

	Stream<Action> actions() {
		return redo.stream();
	}

	void shrink() {
		undo.clear();
		undo.trimToSize();
		redo.trimToSize();
	}

	@Override
	public void toNbt(@NotNull CompoundTag nbt) {
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
		return new Frame(ledger, snapshot == null ? null : snapshot.copy());
	}

}
