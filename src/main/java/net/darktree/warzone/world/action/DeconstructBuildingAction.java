package net.darktree.warzone.world.action;

import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.world.World;
import net.darktree.warzone.world.action.manager.Action;
import net.darktree.warzone.world.entity.building.Building;
import net.querz.nbt.tag.CompoundTag;

public final class DeconstructBuildingAction extends Action {

	private final int x, y;
	private final Building building;
	private final int value;

	public DeconstructBuildingAction(World world, int x, int y) {
		super(world, Actions.DECONSTRUCT);
		this.x = x;
		this.y = y;
		this.building = world.getEntity(x, y, Building.class);
		this.value = Building.remainder(building.getOwner().orElseThrow(), building.getType().value);
	}

	public DeconstructBuildingAction(World world, CompoundTag nbt) {
		this(world, nbt.getInt("x"), nbt.getInt("y"));
	}

	@Override
	public void toNbt(CompoundTag nbt) {
		super.toNbt(nbt);
		nbt.putInt("x", x);
		nbt.putInt("y", y);
	}

	@Override
	protected boolean verify(Symbol symbol) {
		return building.isDeconstructable();
	}

	@Override
	protected void redo(Symbol symbol) {
		building.remove();
		world.getCountry(symbol).addMaterials(value);
	}

	@Override
	protected void undo(Symbol symbol) {
		world.addEntity(building);
		world.getCountry(symbol).addMaterials(-value);
	}

}
