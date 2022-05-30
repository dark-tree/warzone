package net.darktree.core.world.action;

import net.darktree.core.world.World;
import net.darktree.game.country.Symbol;

public abstract class Action {

	boolean verify(World world, Symbol symbol) {
		return true;
	}

	abstract void redo(World world, Symbol symbol);

	abstract void undo(World world, Symbol symbol);
}
