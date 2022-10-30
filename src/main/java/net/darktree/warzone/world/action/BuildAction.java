package net.darktree.warzone.world.action;

import net.darktree.warzone.client.Sounds;
import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.world.World;
import net.darktree.warzone.world.entity.building.Building;
import net.darktree.warzone.world.tile.Surface;
import net.darktree.warzone.world.tile.Tile;
import net.darktree.warzone.world.tile.TilePos;
import net.darktree.warzone.world.tile.TileState;

public class BuildAction extends Action {

	private final Building.Type type;
	private final int x, y;

	private Building building;

	public BuildAction(Building.Type type, int x, int y) {
		this.type = type;
		this.x = x;
		this.y = y;
	}

	@Override
	boolean verify(World world, Symbol symbol) {
		if (type.value > world.getCountry(symbol).getTotalMaterials()) {
			return false;
		}

		for (TilePos pos : type.pattern.list(world, x, y, true)) {
			TileState state = world.getTileState(pos);

			Tile tile = state.getTile();
			if (state.getEntity() != null || state.getOwner() != symbol || tile.getSurface() != Surface.LAND || !tile.canStayOn()) {
				return false;
			}
		}

		return true;
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
