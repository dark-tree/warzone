package net.darktree.warzone.world.action;

import net.darktree.warzone.client.Sounds;
import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.world.World;
import net.darktree.warzone.world.entity.UnitEntity;
import net.darktree.warzone.world.entity.building.CapitolBuilding;
import net.darktree.warzone.world.path.Path;
import net.darktree.warzone.world.tile.TilePos;
import net.darktree.warzone.world.tile.tiles.Tiles;

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
