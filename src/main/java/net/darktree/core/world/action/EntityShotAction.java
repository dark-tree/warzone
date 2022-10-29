package net.darktree.core.world.action;

import net.darktree.core.world.World;
import net.darktree.core.world.entity.UnitEntity;
import net.darktree.game.country.Symbol;

public class EntityShotAction extends Action {

	private final UnitEntity source;
	private final UnitEntity target;
	private boolean killed = false;

	public EntityShotAction(UnitEntity source, UnitEntity target) {
		this.source = source;
		this.target = target;
	}

	@Override
	boolean verify(World world, Symbol symbol) {
		return !source.hasMoved() && world.getCountry(symbol).ammo > 0;
	}

	@Override
	void redo(World world, Symbol symbol) {
		killed = !target.armored;
		world.getCountry(symbol).ammo --;
		source.setAttacked(true);

		if (killed) {
			world.getEntities().remove(target);
		} else {
			target.armored = false;
		}
	}

	@Override
	void undo(World world, Symbol symbol) {
		world.getCountry(symbol).ammo ++;
		source.setAttacked(false);

		if (killed) {
			world.getEntities().add(target);
		} else {
			target.armored = true;
		}
	}

}
