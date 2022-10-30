package net.darktree.warzone.world.action;

import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.world.World;
import net.darktree.warzone.world.entity.building.Building;

public class DeconstructBuildingAction extends Action {

	private final int x, y;
	private final Building building;
	private final int value;

	public DeconstructBuildingAction(Building building, int x, int y) {
		this.x = x;
		this.y = y;
		this.building = building;
		this.value = (int) Math.floor(building.getType().value * 0.4f);
	}

	@Override
	boolean verify(World world, Symbol symbol) {
		return building.isDeconstructable();
	}

	@Override
	void redo(World world, Symbol symbol) {
		building.remove();
		world.getCountry(symbol).addMaterials(value);
	}

	@Override
	void undo(World world, Symbol symbol) {
		world.addEntity(building);
		world.getCountry(symbol).addMaterials(-value);
	}

}
