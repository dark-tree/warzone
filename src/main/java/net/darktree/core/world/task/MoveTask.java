package net.darktree.core.world.task;

import net.darktree.core.world.World;
import net.darktree.core.world.entity.MovingEntity;
import net.darktree.core.world.path.Path;
import net.darktree.game.country.Symbol;

public class MoveTask extends Task {

	private final MovingEntity entity;
	private final Path path;

	public MoveTask(MovingEntity entity, Path path) {
		this.entity = entity;
		this.path = path;
	}

	@Override
	boolean verify(World world, Symbol symbol) {
		return !entity.hasMoved();
	}

	@Override
	void redo(World world, Symbol symbol) {
		entity.follow(path);
	}

	@Override
	void undo(World world, Symbol symbol) {
		entity.revert();
	}

}
