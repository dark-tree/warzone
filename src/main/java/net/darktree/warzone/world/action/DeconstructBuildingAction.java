package net.darktree.warzone.world.action;

import net.darktree.warzone.country.Country;
import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.world.WorldSnapshot;
import net.darktree.warzone.world.action.ledger.Action;
import net.darktree.warzone.world.entity.Entity;
import net.darktree.warzone.world.entity.building.Building;
import net.querz.nbt.tag.CompoundTag;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class DeconstructBuildingAction extends Action {

	private final int x, y;
	private final List<Building> parts = new ArrayList<>();
	private final int value;
	private final Optional<Country> owner;

	public DeconstructBuildingAction(int x, int y) {
		super(Actions.DECONSTRUCT);
		this.x = x;
		this.y = y;

		// FIXME
//		Building building =  world.getEntity(x, y, Building.class);
//		int cost = 0;
//
//		if (building instanceof MultipartStructure multipart) {
//			for (TilePos pos : multipart.getStructureParts()) {
//				Building part = world.getEntity(pos.x, pos.y, Building.class);
//				this.parts.add(part);
//				cost += part.getType().value;
//			}
//		} else {
//			cost = building.getType().value;
//			this.parts.add(building);
//		}

		this.value = 0; // cost;
		this.owner = null; // building.getOwner();
	}

	public DeconstructBuildingAction(CompoundTag nbt) {
		this(nbt.getInt("x"), nbt.getInt("y"));
	}

	@Override
	public void toNbt(CompoundTag nbt) {
		super.toNbt(nbt);
		nbt.putInt("x", x);
		nbt.putInt("y", y);
	}

	@Override
	public boolean apply(WorldSnapshot world, boolean animated) {
		Symbol symbol = world.getCurrentSymbol();

		if (!(parts.stream().allMatch(Building::isDeconstructable) && owner.isPresent() && owner.get().symbol == symbol)) {
			return false;
		}

		parts.forEach(Entity::remove);
		world.getCountry(symbol).addMaterials(getReminder());
		return true;
	}

	private int getReminder() {
		return Building.remainder(owner.orElseThrow(), value);
	}

}
