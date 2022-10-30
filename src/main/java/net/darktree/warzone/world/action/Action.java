package net.darktree.warzone.world.action;

import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.world.World;

public abstract class Action {

	boolean verify(World world, Symbol symbol) {
		return true;
	}

	void common(World world, Symbol symbol) {

	}

	abstract void redo(World world, Symbol symbol);

	abstract void undo(World world, Symbol symbol);

}
