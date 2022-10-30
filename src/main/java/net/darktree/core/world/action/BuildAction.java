package net.darktree.core.world.action;

import net.darktree.core.client.Sounds;
import net.darktree.core.util.BuildingType;
import net.darktree.core.world.World;
import net.darktree.core.world.entity.building.Building;
import net.darktree.core.world.tile.TilePos;
import net.darktree.core.world.tile.TileState;
import net.darktree.game.country.Symbol;

import java.util.List;

public class BuildAction extends Action {

	private final BuildingType type;
	private final int x, y;

	private Building building;

	public BuildAction(BuildingType type, int x, int y) {
		this.type = type;
		this.x = x;
		this.y = y;
	}

	@Override
	boolean verify(World world, Symbol symbol) {
		if (type.value > world.getCountry(symbol).getTotalMaterials()) {
			return false;
		}

		List<TilePos> tiles = type.pattern.list(world, x, y, true);
		TileState[][] map = world.getTiles();

		return tiles.stream().filter(pos -> world.getEntity(pos.x, pos.y) == null).map(pos -> map[pos.x][pos.y]).allMatch(state -> state.getOwner() == symbol);
	}

	@Override
	void redo(World world, Symbol symbol) {
		building = (Building) type.create(world, x, y);
		world.addEntity(building);
		world.getCountry(symbol).addMaterials(-type.value);
		Sounds.STAMP.play(x, y);
	}

	@Override
	void undo(World world, Symbol symbol) {
		building.remove();
		world.getCountry(symbol).addMaterials(type.value);
	}

}
