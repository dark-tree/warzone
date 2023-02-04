package net.darktree.warzone.world.action;

import net.darktree.warzone.client.Sounds;
import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.world.World;
import net.darktree.warzone.world.action.manager.Action;
import net.darktree.warzone.world.entity.Entities;
import net.darktree.warzone.world.entity.UnitEntity;
import net.darktree.warzone.world.entity.building.CapitolBuilding;
import net.darktree.warzone.world.path.Path;
import net.darktree.warzone.world.path.PathFinder;
import net.darktree.warzone.world.path.PathFinderConfig;
import net.darktree.warzone.world.tile.TilePos;
import net.querz.nbt.tag.CompoundTag;

public final class SummonAction extends Action {

	private final int sx, sy;
	private final PathFinder pathfinder;
	private final CapitolBuilding building;

	private UnitEntity entity;
	private int tx, ty;
	private Path path;

	public SummonAction(World world, int x, int y) {
		super(world, Actions.SUMMON_UNIT);
		this.sx = x;
		this.sy = y;

		this.building = world.getEntity(x, y, CapitolBuilding.class);
		this.pathfinder = new PathFinder(world, world.getCurrentSymbol(), building::forEachTile, PathFinderConfig.UNIT_SUMMON);
	}

	public SummonAction(World world, CompoundTag nbt) {
		this(world, nbt.getInt("sx"), nbt.getInt("sy"));
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

	@Override
	protected boolean verify(Symbol symbol) {
		return !building.summoned && path != null;
	}

	@Override
	protected void redo(Symbol symbol) {
		TilePos starting = path.getStart();

		entity = (UnitEntity) world.addEntity(Entities.UNIT, starting.x, starting.y);
		entity.follow(path);
		entity.setSymbol(symbol);
		building.summoned = true;
		Sounds.DRAW_THING.play(starting);
	}

	@Override
	protected void undo(Symbol symbol) {
		entity.remove();
		building.summoned = false;
	}

}
