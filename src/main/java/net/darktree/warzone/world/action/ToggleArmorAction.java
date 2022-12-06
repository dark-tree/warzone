package net.darktree.warzone.world.action;

import net.darktree.warzone.client.Sounds;
import net.darktree.warzone.country.Country;
import net.darktree.warzone.country.Resources;
import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.world.World;
import net.darktree.warzone.world.entity.UnitEntity;
import net.querz.nbt.tag.CompoundTag;

public final class ToggleArmorAction extends ToggleableAction {

	private final int x;
	private final int y;
	private final UnitEntity entity;

	public ToggleArmorAction(World world, int x, int y) {
		super(world, Actions.TOGGLE_ARMOR);
		this.x = x;
		this.y = y;
		this.entity = world.getEntity(x, y, UnitEntity.class);
	}

	public ToggleArmorAction(World world, CompoundTag nbt) {
		this(world, nbt.getInt("x"), nbt.getInt("y"));
	}

	@Override
	public void toNbt(CompoundTag nbt) {
		super.toNbt(nbt);
		nbt.putInt("x", x);
		nbt.putInt("y", y);
	}

	@Override
	protected boolean verify(Symbol symbol) {
		return entity.isInHomeland() && (entity.armored || world.getCountry(symbol).getResource(Resources.ARMOR).value > 0);
	}

	@Override
	protected void redo(Symbol symbol) {
		toggle(world.getCountry(symbol));
	}

	@Override
	protected void undo(Symbol symbol) {
		toggle(world.getCountry(symbol));
	}

	@Override
	protected void common(Symbol symbol) {
		Sounds.EQUIP.play(entity);
	}

	private void toggle(Country country) {
		country.addArmor(entity.armored ? 1 : -1);
		entity.armored = !entity.armored;
	}

	@Override
	public boolean supersedes(ToggleableAction peek) {
		return peek instanceof ToggleArmorAction action && action.entity == this.entity;
	}

}
