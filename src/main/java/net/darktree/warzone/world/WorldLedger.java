package net.darktree.warzone.world;

import net.darktree.warzone.network.Side;
import net.darktree.warzone.util.NbtSerializable;
import net.darktree.warzone.util.TriState;
import net.darktree.warzone.util.iterable.BiIterable;
import net.darktree.warzone.world.action.ledger.Action;
import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.ListTag;
import net.querz.nbt.tag.Tag;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Stack;

public class WorldLedger implements NbtSerializable {

	private final WorldAccess access;
	private final Stack<Frame> frames;

	private WorldSnapshot tracking;

	public WorldLedger(WorldAccess access) {
		this.access = access;
		this.frames = new Stack<>();
	}

	@Deprecated
	public WorldAccess getAccess() {
		return access;
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
	public void beginNextFrame() {
		if (!frames.empty()) {
			getFrame().shrink();
		}

		tracking.endFrame();
		tracking.beginFrame();

		Frame frame = new Frame(this, tracking.copy());
		frames.add(frame);
	}

	public Frame getFrame() {
		return frames.peek();
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
			snapshot = access.getRootWorld().copy();
		}

		// apply all actions between the last
		// cached copy and now
		for (ListIterator<Frame> it = skipped.listIterator(); it.hasNext();) {
			WorldSnapshot view = snapshot;
			Frame frame = it.next();

			view.beginFrame();

			frame.actions().forEach(action -> {
				action.redo(view, false);
			});

			if (it.hasNext()) {
				view.endFrame();
			}
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
		TriState state = getFrame().undo(tracking);

		if (state.orDefaultTo(false)) {
			updateTrackingWorld();
		}

		return state.orDefaultTo(true);
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
	}

	@Override
	public void fromNbt(@NotNull CompoundTag nbt) {
		ListTag<?> framesTagList = nbt.getListTag("frames");

		for (Tag<?> tag : framesTagList) {
			Frame frame = Frame.load(null, this, (CompoundTag) tag);
			frame.fromNbt((CompoundTag) tag);
			frames.add(frame);
		}

		updateTrackingWorld();
	}

}
