package net.darktree.core.world.action;

import net.darktree.core.client.Sounds;
import net.darktree.core.world.World;
import net.darktree.core.world.entity.UnitEntity;
import net.darktree.core.world.entity.building.CapitolBuilding;
import net.darktree.core.world.path.Path;
import net.darktree.core.world.tile.TilePos;
import net.darktree.core.world.tiles.Tiles;
import net.darktree.game.country.Symbol;

public class SummonAction extends Action {

	private final Path path;
	private final CapitolBuilding building;

	private UnitEntity entity;

	public SummonAction(Path path, CapitolBuilding building) {
		this.path = path;
		this.building = building;
	}

	@Override
	boolean verify(World world, Symbol symbol) {
		return !building.summoned;
	}

	@Override
	void redo(World world, Symbol symbol) {
		TilePos starting = path.getStart();

		entity = (UnitEntity) world.addEntity(starting.x, starting.y, Tiles.UNIT);
		entity.follow(path);
		entity.setSymbol(symbol);
		building.summoned = true;
		Sounds.DRAW_THING.play(starting);
	}

	@Override
	void undo(World world, Symbol symbol) {
		entity.remove();
		building.summoned = false;
	}

}
