package net.darktree.warzone.world.action;

import net.darktree.warzone.country.Country;
import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.world.World;
import net.darktree.warzone.world.action.manager.Action;
import net.darktree.warzone.world.entity.Entity;
import net.darktree.warzone.world.entity.building.Building;
import net.darktree.warzone.world.entity.building.MultipartStructure;
import net.darktree.warzone.world.tile.TilePos;
import net.querz.nbt.tag.CompoundTag;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class DeconstructBuildingAction extends Action {

	private final int x, y;
	private final List<Building> parts = new ArrayList<>();
	private final int value;
	private final Optional<Country> owner;

	public DeconstructBuildingAction(World world, int x, int y) {
		super(world, Actions.DECONSTRUCT);
		this.x = x;
		this.y = y;

		Building building = world.getEntity(x, y, Building.class);
		int cost = 0;

		if (building instanceof MultipartStructure multipart) {
			for (TilePos pos : multipart.getStructureParts()) {
				Building part = world.getEntity(pos.x, pos.y, Building.class);
				this.parts.add(part);
				cost += part.getType().value;
			}
		} else {
			cost = building.getType().value;
			this.parts.add(building);
		}

		this.value = cost;
		this.owner = building.getOwner();
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

	private int getReminder() {
		return Building.remainder(owner.orElseThrow(), value);
	}

	@Override
	protected boolean verify(Symbol symbol) {
		return parts.stream().allMatch(Building::isDeconstructable) && owner.isPresent() && owner.get().symbol == symbol;
	}

	@Override
	protected void redo(Symbol symbol) {
		parts.forEach(Entity::remove);
		world.getCountry(symbol).addMaterials(getReminder());
	}

	@Override
	protected void undo(Symbol symbol) {
		parts.forEach(world::addEntity);
		world.getCountry(symbol).addMaterials(-getReminder());
	}

}
