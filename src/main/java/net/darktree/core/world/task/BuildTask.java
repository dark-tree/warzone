package net.darktree.core.world.task;

import net.darktree.core.util.Type;
import net.darktree.core.world.World;
import net.darktree.core.world.tile.TilePos;
import net.darktree.core.world.tile.TileState;
import net.darktree.game.buildings.Building;
import net.darktree.game.country.Symbol;

import java.util.List;

public class BuildTask extends Task {

	private final Type<Building> type;
	private final int x, y;
	private Building building;

	public BuildTask(Type<Building> type, int x, int y) {
		this.type = type;
		this.x = x;
		this.y = y;
	}

	@Override
	public void prepare(World world, Symbol symbol) {
		this.building = type.construct(world, x, y);
	}

	@Override
	boolean verify(World world, Symbol symbol) {
		if (building.getCost() > world.getCountry(symbol).getTotalMaterials()) {
			return false;
		}

		List<TilePos> tiles = building.getPattern().list(world, x, y, true);
		TileState[][] map = world.getTiles();

		return tiles.stream().filter(pos -> world.getEntity(pos.x, pos.y) == null).map(pos -> map[pos.x][pos.y]).allMatch(state -> state.getTile().isReplaceable() && state.getOwner() == symbol);
	}

	@Override
	void redo(World world, Symbol symbol) {
		world.placeBuilding(x, y, building);
		world.getCountry(symbol).addMaterials(-building.getCost());
	}

	@Override
	void undo(World world, Symbol symbol) {
		building.removed();
	}

}
