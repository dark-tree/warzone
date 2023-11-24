package net.darktree.warzone.world.action;

import net.darktree.warzone.country.Resources;
import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.world.WorldSnapshot;
import net.darktree.warzone.world.action.ledger.Action;
import net.darktree.warzone.world.entity.UnitEntity;
import net.querz.nbt.tag.CompoundTag;

public final class EntityShotAction extends Action {

	private final int sx, sy, tx, ty;

	public EntityShotAction(int sx, int sy, int tx, int ty) {
		super(Actions.ENTITY_SHOT);
		this.sx = sx;
		this.sy = sy;
		this.tx = tx;
		this.ty = ty;
	}

	public EntityShotAction(CompoundTag nbt) {
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
		Symbol symbol = world.getCurrentSymbol();
		UnitEntity source = world.getEntity(sx, sy, UnitEntity.class);
		UnitEntity target = world.getEntity(tx, ty, UnitEntity.class);

		if (!(!source.hasActed() && world.getCountry(symbol).getResource(Resources.AMMO).has(1))) {
			return false;
		}

		world.getCountry(symbol).getResource(Resources.AMMO).dec();
		source.setAttacked(true);

		if (!target.armored) {
			target.remove();
		} else {
			target.armored = false;
		}

		return true;
	}


}
