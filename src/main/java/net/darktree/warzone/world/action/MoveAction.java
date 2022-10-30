package net.darktree.warzone.world.action;

import net.darktree.warzone.client.Sounds;
import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.world.World;
import net.darktree.warzone.world.entity.MovingEntity;
import net.darktree.warzone.world.path.Path;

public class MoveAction extends Action {

	private final MovingEntity entity;
	private final Path path;

	public MoveAction(MovingEntity entity, Path path) {
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

	@Override
	void common(World world, Symbol symbol) {
		Sounds.DRAW_PATH.play(entity);
	}
}
