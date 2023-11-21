package net.darktree.warzone.world.action;

import net.darktree.warzone.client.Sounds;
import net.darktree.warzone.country.Country;
import net.darktree.warzone.country.Resources;
import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.world.WorldSnapshot;
import net.darktree.warzone.world.action.ledger.Action;
import net.darktree.warzone.world.entity.UnitEntity;
import net.querz.nbt.tag.CompoundTag;

public final class ColonizeAction extends Action {

	private final int x, y;
	private final int dice;
	private final boolean war;

	public ColonizeAction(int dice, int x, int y, boolean war) {
		super(Actions.COLONIZE);
		this.x = x;
		this.y = y;
		this.dice = dice;
		this.war = war;
	}

	public ColonizeAction(CompoundTag nbt) {
		this(nbt.getInt("dice"), nbt.getInt("x"), nbt.getInt("y"), nbt.getBoolean("war"));
	}

	@Override
	public void toNbt(CompoundTag nbt) {
		super.toNbt(nbt);
		nbt.putInt("dice", dice);
		nbt.putInt("x", x);
		nbt.putInt("y", y);
		nbt.putBoolean("war", war);
	}

	@Override
	public boolean apply(WorldSnapshot world, boolean animated) {
		Symbol symbol = world.getCurrentSymbol();
		Country country = world.getCountry(symbol);
		UnitEntity entity = world.getEntity(x, y, UnitEntity.class);

		if (entity == null) {
			return false;
		}

		if (!((!war || country.getResource(Resources.AMMO).has(2)) && country.canColonize() && !entity.hasMoved() && world.canControl(entity.getX(), entity.getY(), symbol))) {
			return false;
		}

		world.getCountry(symbol).onColonize();
		entity.colonize(dice, this.war);
		Sounds.DICE_ROLL.play(entity).setVolume(2);

		if (war) {
			world.getCountry(symbol).getResource(Resources.AMMO).take(2);
		}

		return true;
	}

	@Override
	public boolean isRevertible() {
		return false;
	}
}
