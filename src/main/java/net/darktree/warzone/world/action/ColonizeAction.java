package net.darktree.warzone.world.action;

import net.darktree.warzone.client.Sounds;
import net.darktree.warzone.country.Resources;
import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.world.World;
import net.darktree.warzone.world.action.manager.Action;
import net.darktree.warzone.world.action.manager.FinalAction;
import net.darktree.warzone.world.action.manager.HostAction;
import net.darktree.warzone.world.entity.UnitEntity;
import net.querz.nbt.tag.CompoundTag;

public final class ColonizeAction extends Action implements FinalAction, HostAction {

	private final int x, y;
	private final UnitEntity entity;
	private final int dice;
	private final boolean war;

	public ColonizeAction(World world, int dice, int x, int y, boolean war) {
		super(world, Actions.COLONIZE);
		this.x = x;
		this.y = y;
		this.entity = world.getEntity(x, y, UnitEntity.class);
		this.dice = dice;
		this.war = war;
	}

	public ColonizeAction(World world, CompoundTag nbt) {
		this(world, nbt.getInt("dice"), nbt.getInt("x"), nbt.getInt("y"), nbt.getBoolean("war"));
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
	protected boolean verify(Symbol symbol) {
		return (!war || world.getCountry(symbol).getResource(Resources.AMMO).has(2)) && !world.getCountry(symbol).colonized && !entity.hasMoved() && world.canControl(entity.getX(), entity.getY(), symbol);
	}

	@Override
	protected void redo(Symbol symbol) {
		world.getCountry(symbol).colonized = true;
		entity.colonize(dice, this.war);
		Sounds.DICE_ROLL.play(entity).setVolume(2);

		if (war) {
			world.getCountry(symbol).getResource(Resources.AMMO).take(2);
		}
	}

	@Override
	protected void undo(Symbol symbol) {
		throw new UnsupportedOperationException("Colonization cannot be undone!");
	}

}
