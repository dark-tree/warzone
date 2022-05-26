package net.darktree.core.world.task;

import net.darktree.core.world.World;
import net.darktree.game.country.Symbol;

public abstract class Task {

	boolean verify(World world, Symbol symbol) {
		return true;
	}

	public void prepare(World world, Symbol symbol) {

	}

	abstract void redo(World world, Symbol symbol);

	abstract void undo(World world, Symbol symbol);
}
