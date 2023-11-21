package net.darktree.warzone.world.action;

import net.darktree.warzone.client.Sounds;
import net.darktree.warzone.country.Resources;
import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.world.Update;
import net.darktree.warzone.world.WorldSnapshot;
import net.darktree.warzone.world.action.ledger.Action;
import net.darktree.warzone.world.entity.UnitEntity;
import net.querz.nbt.tag.CompoundTag;

public final class ToggleArmorAction extends ToggleableAction {

	private final int x;
	private final int y;

	public ToggleArmorAction(int x, int y) {
		super(Actions.TOGGLE_ARMOR);
		this.x = x;
		this.y = y;
	}

	public ToggleArmorAction(CompoundTag nbt) {
		this(nbt.getInt("x"), nbt.getInt("y"));
	}

	@Override
	public void toNbt(CompoundTag nbt) {
		super.toNbt(nbt);
		nbt.putInt("x", x);
		nbt.putInt("y", y);
	}

	@Override
	public boolean apply(WorldSnapshot world, boolean animated) {
		Symbol symbol = world.getCurrentSymbol();
		UnitEntity entity = world.getEntity(x, y, UnitEntity.class);

		if (!(entity.isInHomeland() && (entity.armored || world.getCountry(symbol).getResource(Resources.ARMOR).has(1)))) {
			return false;
		}

		world.getCountry(symbol).getResource(Resources.ARMOR).add(entity.armored ? 1 : -1);
		entity.armored = !entity.armored;
		world.pushUpdateBits(Update.OVERLAY);
		Sounds.EQUIP.play(entity);
		return true;
	}

	@Override
	public boolean supersedes(Action peek) {
		return peek instanceof ToggleArmorAction action && action.x == this.x && action.y == this.y;
	}

}
