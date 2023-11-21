package net.darktree.warzone.world;

import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.network.Side;
import net.darktree.warzone.util.NbtSerializable;
import net.darktree.warzone.util.iterable.BiIterable;
import net.darktree.warzone.world.action.ledger.Action;
import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.ListTag;
import net.querz.nbt.tag.Tag;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class WorldLedger implements NbtSerializable {

	private final WorldAccess view;
	private final WorldSnapshot root;
	private final Stack<Frame> frames;

	private WorldSnapshot tracking;
	private int cycle = 1;
	private int turn = 0;

	public WorldLedger(WorldAccess view, WorldSnapshot root) {
		this.view = view;
		this.root = root;
		this.frames = new Stack<>();
	}

	// TODO should this be renamed?
	public WorldAccess getView() {
		return view;
	}

	/**
	 * Returns a snapshot of the world as it was when the game started
	 */
	public WorldSnapshot getRootWorld() {
		return root;
	}

	/**
	 * Returns an up-to-date snapshot of the world
	 */
	public WorldSnapshot getTrackingWorld() {
		return tracking;
	}

	/**
	 * Marks the beginning of a turn
	 */
	public void beginNextFrame(WorldInfo info) {
		if (!frames.empty()) {
			getFrame().shrink();
		}

		int players = info.getSymbols().size();
		this.turn = (turn + 1) % players;

		if (turn == 0) {
			cycle ++;
		}

		Symbol symbol = info.symbols.get(this.turn);
		frames.add(new Frame(this, symbol, tracking));
	}

	public Frame getFrame() {
		return frames.peek();
	}

	public int getTurn() {
		return turn;
	}

	public int getCycle() {
		return cycle;
	}

	// FIXME unimplemented
	public boolean isLocal() {
		return true;
	}

	// FIXME unimplemented
	public Side getSide() {
		return Side.HOST;
	}

	private void updateTrackingWorld() {
		WorldSnapshot snapshot = null;
		List<Frame> skipped = new ArrayList<>();

		// search for the last cached world state
		for (Frame frame : BiIterable.reversed(frames)) {
			WorldSnapshot world = frame.getWorld();

			if (world != null) {
				snapshot = world;
			}

			skipped.add(frame);

			// if we found it, break
			if (snapshot != null) {
				break;
			}
		}

		// there is no cached world snapshot
		// we need to recreate tracking from root
		if (snapshot == null) {
			snapshot = getRootWorld().copy(getFrame());
		}

		// apply all actions between the last
		// cached copy and now
		for (Frame frame : skipped) {
			final WorldSnapshot view = snapshot;

			frame.actions().forEach(action -> {
				action.apply(view, false);
			});
		}

		this.tracking = snapshot;
	}

	public boolean push(Action action) {
		return getFrame().push(action, tracking);
	}

	// TODO
	public boolean push(Action action, boolean remote) {
		return !remote && push(action);
	}

	public boolean undo() {
		if (getFrame().undo()) {
			updateTrackingWorld();
			return true;
		}

		return false;
	}

	public boolean redo() {
		return getFrame().redo(tracking);
	}

	public boolean canUndo() {
		return getFrame().canUndo();
	}

	@Override
	public void toNbt(@NotNull CompoundTag nbt) {
		ListTag<CompoundTag> framesTagList = new ListTag<>(CompoundTag.class);

		for (Frame frame : frames) {
			CompoundTag frameTag = new CompoundTag();
			frame.toNbt(frameTag);
			framesTagList.add(frameTag);
		}

		nbt.put("frames", framesTagList);
		nbt.putInt("cycle", cycle);
		nbt.putInt("turn", turn);
	}

	@Override
	public void fromNbt(@NotNull CompoundTag nbt) {
		ListTag<?> framesTagList = nbt.getListTag("frames");
		this.cycle = nbt.getInt("cycle");
		this.turn = nbt.getInt("turn");

		for (Tag<?> tag : framesTagList) {
			Frame frame = Frame.load(null, this, (CompoundTag) tag);
			frame.fromNbt((CompoundTag) tag);
			frames.add(frame);
		}

		updateTrackingWorld();
	}

}
