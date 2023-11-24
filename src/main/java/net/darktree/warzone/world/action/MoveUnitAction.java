package net.darktree.warzone.world.action;

import net.darktree.warzone.client.Sounds;
import net.darktree.warzone.world.WorldSnapshot;
import net.darktree.warzone.world.action.ledger.Action;
import net.darktree.warzone.world.action.ledger.UndoBehaviour;
import net.darktree.warzone.world.entity.UnitEntity;
import net.darktree.warzone.world.path.Path;
import net.darktree.warzone.world.path.PathFinder;
import net.darktree.warzone.world.tile.TilePos;
import net.querz.nbt.tag.CompoundTag;

public final class MoveUnitAction extends Action {

	private final int sx, sy, tx, ty;

	public MoveUnitAction(int sx, int sy, int tx, int ty) {
		super(Actions.ENTITY_MOVE);
		this.sx = sx;
		this.sy = sy;
		this.tx = tx;
		this.ty = ty;
	}

	public MoveUnitAction(CompoundTag nbt) {
		this(nbt.getInt("sx"), nbt.getInt("sy"), nbt.getInt("tx"), nbt.getInt("ty"));
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
	public boolean redo(WorldSnapshot world, boolean animate) {
		UnitEntity unit = world.getEntity(sx, sy, UnitEntity.class);

		if (unit == null || unit.hasActed()) {
			return false;
		}

		PathFinder pathfinder = unit.getPathFinder(false);
		Path path = pathfinder.getPathTo(tx, ty);

		if (path == null) {
			return false;
		}

		if (animate) {
			unit.follow(path);
			Sounds.DRAW_PATH.play(unit);
		} else {
			TilePos pos = path.getEnd();
			unit.setPos(pos.x, pos.y);
		}

		return true;
	}

	@Override
	public boolean undo(WorldSnapshot world) {
		UnitEntity unit = world.getEntity(tx, ty, UnitEntity.class);

		if (unit == null || !unit.hasMoved()) {
			return false;
		}

		unit.revert();
		return true;
	}

	@Override
	public UndoBehaviour getUndoBehaviour() {
		return UndoBehaviour.JUST_DROP;
	}

}
