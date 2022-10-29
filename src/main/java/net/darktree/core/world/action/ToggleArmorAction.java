package net.darktree.core.world.action;

import net.darktree.core.client.Sounds;
import net.darktree.core.world.World;
import net.darktree.core.world.entity.UnitEntity;
import net.darktree.game.country.Country;
import net.darktree.game.country.Symbol;

public class ToggleArmorAction extends ToggleableAction {

	private final UnitEntity entity;

	public ToggleArmorAction(UnitEntity entity) {
		this.entity = entity;
	}

	@Override
	boolean verify(World world, Symbol symbol) {
		return entity.isInHomeland() && (entity.armored || world.getCountry(symbol).armor > 0);
	}

	@Override
	void redo(World world, Symbol symbol) {
		toggle(world.getCountry(symbol));
	}

	@Override
	void undo(World world, Symbol symbol) {
		toggle(world.getCountry(symbol));
	}

	@Override
	void common(World world, Symbol symbol) {
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
