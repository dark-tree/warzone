package net.darktree.warzone.world.action;

import net.darktree.warzone.client.Sounds;
import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.world.WorldSnapshot;
import net.darktree.warzone.world.action.ledger.Action;
import net.darktree.warzone.world.entity.Entities;
import net.darktree.warzone.world.entity.UnitEntity;
import net.darktree.warzone.world.entity.building.CapitolBuilding;
import net.darktree.warzone.world.path.Path;
import net.darktree.warzone.world.path.PathFinder;
import net.darktree.warzone.world.tile.TilePos;
import net.querz.nbt.tag.CompoundTag;

public final class SummonAction extends Action {

	private final int sx, sy;
	//private final PathFinder pathfinder;
	//private final CapitolBuilding building;

	private int tx, ty;
//	private Path path;

	public SummonAction(int x, int y) {
		super(Actions.SUMMON_UNIT);
		this.sx = x;
		this.sy = y;

//		this.building = null; // world.getEntity(x, y, CapitolBuilding.class); FIXME
//		this.pathfinder = building.getPathFinder();
	}

	public SummonAction(CompoundTag nbt) {
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
		CapitolBuilding building = world.getEntity(sx, sy, CapitolBuilding.class);
		PathFinder pathfinder = building.getPathFinder();
		Path path = pathfinder.getPathTo(tx, ty);

		if (!(!building.summoned && path != null)) {
			return false;
		}

		Symbol symbol = world.getCurrentSymbol();
		TilePos starting = path.getStart();

		UnitEntity entity = (UnitEntity) world.addEntity(Entities.UNIT, starting.x, starting.y);
		entity.follow(path);
		entity.setSymbol(symbol);
		building.summoned = true;
		Sounds.DRAW_THING.play(starting);

		return true;
	}

	public PathFinder getPathfinder() {
		return null; // FIXME
	}

	public boolean setTarget(int x, int y) {
		//if (pathfinder.canReach(x, y)) {
			tx = x;
			ty = y;
		//	path = pathfinder.getPathTo(x, y);
			return true;
		//}

		//return false;
	}

}
