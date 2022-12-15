package net.darktree.warzone.world.action;

import net.darktree.warzone.client.Sounds;
import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.world.World;
import net.darktree.warzone.world.action.manager.Action;
import net.darktree.warzone.world.entity.UnitEntity;
import net.darktree.warzone.world.path.Path;
import net.darktree.warzone.world.path.PathFinder;
import net.darktree.warzone.world.path.PathFinderConfig;
import net.darktree.warzone.world.pattern.Patterns;
import net.querz.nbt.tag.CompoundTag;

public final class MoveUnitAction extends Action {

	private final int sx, sy;
	private final UnitEntity entity;
	private final PathFinder pathfinder;

	private int tx, ty;
	private Path path;

	public MoveUnitAction(World world, int x, int y) {
		super(world, Actions.ENTITY_MOVE);
		this.sx = x;
		this.sy = y;
		this.entity = world.getEntity(x, y, UnitEntity.class);

		Symbol owner = world.getTileState(x, y).getOwner();
		this.pathfinder = new PathFinder(world, world.getCurrentSymbol(), Patterns.IDENTITY.place(world, x, y), PathFinderConfig.getForUnitAt(this.entity, owner));
	}

	public MoveUnitAction(World world, CompoundTag nbt) {
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
		return entity != null && !entity.hasActed() && path != null;
	}

	@Override
	protected void redo(Symbol symbol) {
		entity.follow(path);
	}

	@Override
	protected void undo(Symbol symbol) {
		entity.revert();
	}

	@Override
	protected void common(Symbol symbol) {
		Sounds.DRAW_PATH.play(entity);
	}
}
