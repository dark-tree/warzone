package net.darktree.core.world.action;

import net.darktree.core.world.World;
import net.darktree.core.world.path.Path;
import net.darktree.core.world.path.Pathfinder;
import net.darktree.core.world.tile.TilePos;
import net.darktree.game.buildings.Building;
import net.darktree.game.country.Symbol;
import net.darktree.game.entities.UnitEntity;
import net.darktree.game.tiles.Tiles;

public class SummonAction extends Action {

	private final int x, y;
	private final Pathfinder pathfinder;

	private UnitEntity entity;

	public SummonAction(World world, Symbol symbol, int x, int y) {
		this.x = x;
		this.y = y;

		Building capitol = world.getCountry(symbol).getCapitol();
		pathfinder = new Pathfinder(world, capitol.x, capitol.y, 10, symbol, capitol.getPattern(), false);
	}

	@Override
	boolean verify(World world, Symbol symbol) {
		return pathfinder.canReach(x, y);
	}

	@Override
	void redo(World world, Symbol symbol) {
		Path path = pathfinder.getPathTo(x, y);
		TilePos starting = path.getStart();

		entity = (UnitEntity) world.addEntity(starting.x, starting.y, Tiles.TEST);
		entity.follow(path);
	}

	@Override
	void undo(World world, Symbol symbol) {
		entity.removed = true;
	}
}
