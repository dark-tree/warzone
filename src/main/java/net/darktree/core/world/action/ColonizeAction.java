package net.darktree.core.world.action;

import net.darktree.core.world.World;
import net.darktree.game.country.Symbol;
import net.darktree.game.entities.UnitEntity;

public class ColonizeAction extends Action {

	private final UnitEntity entity;
	private final int dice;

	public ColonizeAction(UnitEntity entity, int dice) {
		this.entity = entity;
		this.dice = dice;
	}

	@Override
	boolean verify(World world, Symbol symbol) {
		return !world.getCountry(symbol).colonized && !entity.hasMoved() && world.canControl(entity.getX(), entity.getY(), symbol);
	}

	@Override
	void redo(World world, Symbol symbol) {
		world.getCountry(symbol).colonized = true;
		entity.colonize(dice);

		// colonization can not be undone or modified
		world.getManager().pointOfNoReturn(symbol);
	}

	@Override
	void undo(World world, Symbol symbol) {
		throw new UnsupportedOperationException("Colonization cannot be undone!");
	}

}
