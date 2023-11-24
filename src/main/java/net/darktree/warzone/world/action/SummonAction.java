package net.darktree.warzone.world.action;

import net.darktree.warzone.client.Sounds;
import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.world.WorldSnapshot;
import net.darktree.warzone.world.action.ledger.Action;
import net.darktree.warzone.world.action.ledger.UndoBehaviour;
import net.darktree.warzone.world.entity.Entities;
import net.darktree.warzone.world.entity.UnitEntity;
import net.darktree.warzone.world.entity.building.CapitolBuilding;
import net.darktree.warzone.world.path.Path;
import net.darktree.warzone.world.path.PathFinder;
import net.darktree.warzone.world.tile.TilePos;
import net.querz.nbt.tag.CompoundTag;

public final class SummonAction extends Action {

	private final int sx, sy, tx, ty;

	public SummonAction(int sx, int sy, int tx, int ty) {
		super(Actions.SUMMON_UNIT);
		this.sx = sx;
		this.sy = sy;
		this.tx = tx;
		this.ty = ty;
	}

	public SummonAction(CompoundTag nbt) {
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
		CapitolBuilding building = world.getEntity(sx, sy, CapitolBuilding.class);

		if (building == null || building.summoned) {
			return false;
		}

		PathFinder pathfinder = building.getPathFinder();
		Path path = pathfinder.getPathTo(tx, ty);

		if (path == null) {
			return false;
		}

		Symbol symbol = world.getCurrentSymbol();
		TilePos starting = path.getStart();
		TilePos ending = path.getEnd();

		if (animate) {
			UnitEntity entity = (UnitEntity) world.addEntity(Entities.UNIT, starting.x, starting.y);
			entity.follow(path);
			entity.setSymbol(symbol);
			Sounds.DRAW_THING.play(starting);
		} else {
			UnitEntity entity = (UnitEntity) world.addEntity(Entities.UNIT, starting.x, starting.y);
			entity.setPos(ending.x, ending.y);
			entity.setSymbol(symbol);
		}

		building.summoned = true;
		return true;
	}

	@Override
	public boolean undo(WorldSnapshot world) {
		UnitEntity unit = world.getEntity(tx, ty, UnitEntity.class);
		CapitolBuilding building = world.getEntity(sx, sy, CapitolBuilding.class);

		if (building == null || !building.summoned) {
			return false;
		}

		if (unit == null || !unit.hasMoved()) {
			return false;
		}

		unit.remove();
		building.summoned = false;
		return true;
	}

	@Override
	public UndoBehaviour getUndoBehaviour() {
		return UndoBehaviour.JUST_DROP;
	}

}
