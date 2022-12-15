package net.darktree.warzone.world.action;

import net.darktree.warzone.country.Resources;
import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.world.World;
import net.darktree.warzone.world.action.manager.Action;
import net.darktree.warzone.world.entity.UnitEntity;
import net.querz.nbt.tag.CompoundTag;

public final class EntityShotAction extends Action {

	private final int sx, sy, tx, ty;
	private final UnitEntity source;
	private final UnitEntity target;
	private boolean killed = false;

	public EntityShotAction(World world, int sx, int sy, int tx, int ty) {
		super(world, Actions.ENTITY_SHOT);
		this.sx = sx;
		this.sy = sy;
		this.tx = tx;
		this.ty = ty;
		this.source = world.getEntity(sx, sy, UnitEntity.class);
		this.target = world.getEntity(tx, ty, UnitEntity.class);
	}

	public EntityShotAction(World world, CompoundTag nbt) {
		this(world, nbt.getInt("sx"), nbt.getInt("sy"), nbt.getInt("tx"), nbt.getInt("ty"));
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
	protected boolean verify(Symbol symbol) {
		return !source.hasActed() && world.getCountry(symbol).getResource(Resources.AMMO).value > 0;
	}

	@Override
	protected void redo(Symbol symbol) {
		killed = !target.armored;
		world.getCountry(symbol).getResource(Resources.AMMO).value --;
		source.setAttacked(true);

		if (killed) {
			target.remove();
		} else {
			target.armored = false;
		}
	}

	@Override
	protected void undo(Symbol symbol) {
		world.getCountry(symbol).getResource(Resources.AMMO).value ++;
		source.setAttacked(false);

		if (killed) {
			world.addEntity(target);
		} else {
			target.armored = true;
		}
	}

}
