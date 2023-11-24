package net.darktree.warzone.world.action;

import com.google.common.collect.ImmutableList;
import net.darktree.warzone.country.Country;
import net.darktree.warzone.country.Resources;
import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.world.WorldSnapshot;
import net.darktree.warzone.world.action.ledger.Action;
import net.darktree.warzone.world.entity.Entity;
import net.darktree.warzone.world.entity.building.Building;
import net.darktree.warzone.world.entity.building.MultipartStructure;
import net.querz.nbt.tag.CompoundTag;

import java.util.List;
import java.util.Optional;

public final class DeconstructBuildingAction extends Action {

	private final int x, y;

	public DeconstructBuildingAction(int x, int y) {
		super(Actions.DECONSTRUCT);
		this.x = x;
		this.y = y;
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
	public boolean redo(WorldSnapshot world, boolean animate) {
		Building building = world.getEntity(x, y, Building.class);
		Symbol symbol = world.getCurrentSymbol();
		List<Building> parts;
		int value = 0;

		if (building == null) {
			return false;
		}

		Optional<Country> owner = building.getOwner();

		// verify ownership
		if (owner.isEmpty() || owner.get().symbol != symbol) {
			return false;
		}

		if (building instanceof MultipartStructure multipart) {
			parts = multipart.getStructureParts(world);

			for (Building part : parts) {
				value += part.getType().value;
			}
		} else {
			parts = ImmutableList.of(building);
			value = building.getType().value;
		}

		parts.forEach(Entity::remove);
		world.getCountry(symbol).getResource(Resources.MATERIALS).add(Building.remainder(owner.orElseThrow(), value));
		return true;
	}

}
