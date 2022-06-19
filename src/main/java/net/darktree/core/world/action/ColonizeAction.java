package net.darktree.core.world.action;

import net.darktree.core.client.Sounds;
import net.darktree.core.world.World;
import net.darktree.game.country.Symbol;
import net.darktree.game.entities.UnitEntity;

public class ColonizeAction extends Action {

	private final UnitEntity entity;
	private final int dice;
	private final boolean war;

	public ColonizeAction(UnitEntity entity, int dice, boolean war) {
		this.entity = entity;
		this.dice = dice;
		this.war = war;
	}

	@Override
	boolean verify(World world, Symbol symbol) {
		return (!war || world.getCountry(symbol).ammo >= 2) && !world.getCountry(symbol).colonized && !entity.hasMoved() && world.canControl(entity.getX(), entity.getY(), symbol);
	}

	@Override
	void redo(World world, Symbol symbol) {
		world.getCountry(symbol).colonized = true;
		entity.colonize(dice, this.war);
		Sounds.DICE_ROLL.play(entity).setVolume(2);

		if (war) {
			world.getCountry(symbol).ammo -= 2;
		}

		// colonization can not be undone or modified
		world.getManager().pointOfNoReturn(symbol);
	}

	@Override
	void undo(World world, Symbol symbol) {
		throw new UnsupportedOperationException("Colonization cannot be undone!");
	}

}
