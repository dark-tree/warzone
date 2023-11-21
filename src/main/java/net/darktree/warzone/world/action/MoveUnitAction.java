package net.darktree.warzone.world.action;

import net.darktree.warzone.client.Sounds;
import net.darktree.warzone.world.WorldSnapshot;
import net.darktree.warzone.world.action.ledger.Action;
import net.darktree.warzone.world.entity.UnitEntity;
import net.darktree.warzone.world.path.Path;
import net.darktree.warzone.world.path.PathFinder;
import net.querz.nbt.tag.CompoundTag;

public final class MoveUnitAction extends Action {

	private final int sx, sy;
	private final UnitEntity entity;
	private final PathFinder pathfinder;

	private int tx, ty;
	private Path path;

	public MoveUnitAction(int x, int y) {
		super(Actions.ENTITY_MOVE);
		this.sx = x;
		this.sy = y;
		this.entity = null; // world.getEntity(x, y, UnitEntity.class); FIXME
		this.pathfinder = this.entity.getPathFinder(false);
	}

	public MoveUnitAction(CompoundTag nbt) {
		this(nbt.getInt("sx"), nbt.getInt("sy"));
		setTarget(nbt.getInt("tx"), nbt.getInt("ty"));
	}

	@Override
	public void toNbt(CompoundTag nbt) {
		super.toNbt(nbt);
		nbt.putInt("sx", sx);
		nbt.putInt("sy", sy);
		nbt.putInt("tx", tx);
		nbt.putInt("ty", ty);
	}

	@Override
	public boolean apply(WorldSnapshot world, boolean animated) {
		if (!(entity != null && !entity.hasActed() && path != null)) {
			return false;
		}

		entity.follow(path);
		Sounds.DRAW_PATH.play(entity);
		return true;
	}

	public PathFinder getPathfinder() {
		return pathfinder;
	}

	public boolean setTarget(int x, int y) {
		if (pathfinder.canReach(x, y)) {
			tx = x;
			ty = y;
			path = pathfinder.getPathTo(x, y);
			return true;
		}

		return false;
	}

}
