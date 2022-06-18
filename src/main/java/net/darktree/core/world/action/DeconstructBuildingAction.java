package net.darktree.core.world.action;

import net.darktree.core.world.World;
import net.darktree.game.buildings.Building;
import net.darktree.game.country.Symbol;

public class DeconstructBuildingAction extends Action {

	private final int x, y;
	private final Building building;
	private final int value;

	public DeconstructBuildingAction(Building building, int x, int y) {
		this.x = x;
		this.y = y;
		this.building = building;
		this.value = (int) Math.floor(building.type.value * 0.4f);
	}

	@Override
	boolean verify(World world, Symbol symbol) {
		return building.isDeconstructable(world, this.x, this.y);
	}

	@Override
	void redo(World world, Symbol symbol) {
		building.remove();
		world.getCountry(symbol).addMaterials(value);
	}

	@Override
	void undo(World world, Symbol symbol) {
		world.placeBuilding(x, y, building);
		world.getCountry(symbol).addMaterials(-value);
	}

}
