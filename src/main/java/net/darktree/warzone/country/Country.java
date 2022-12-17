package net.darktree.warzone.country;

import net.darktree.warzone.Registries;
import net.darktree.warzone.event.TurnEvent;
import net.darktree.warzone.util.NbtSerializable;
import net.darktree.warzone.util.math.MutableInt;
import net.darktree.warzone.world.WorldListener;
import net.darktree.warzone.world.entity.building.Building;
import net.darktree.warzone.world.entity.building.CapitolBuilding;
import net.darktree.warzone.world.entity.building.MineBuilding;
import net.querz.nbt.tag.CompoundTag;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

public class Country implements NbtSerializable, WorldListener {

	private final List<Building> buildings = new ArrayList<>();
	public final Symbol symbol;
	public boolean colonized = false;

	public int income = 0;

	private final Map<Resource, MutableInt> resources;

	public Country(Symbol symbol) {
		this.symbol = symbol;
		this.resources = Registries.RESOURCES.map(new IdentityHashMap<>(), resource -> new MutableInt(0));
	}

	@Override
	public void toNbt(@NotNull CompoundTag tag) {
		tag.putByte("symbol", (byte) symbol.ordinal());
		resources.forEach((resource, entry) -> {
			tag.putInt(resource.key(), entry.value);
		});
	}

	@Override
	public void fromNbt(@NotNull CompoundTag tag) {
		resources.forEach((resource, entry) -> {
			entry.value = tag.getInt(resource.key());
		});
	}

	@Override
	public void onPlayerTurnEvent(TurnEvent event, Symbol symbol) {
		if (symbol == this.symbol && event == TurnEvent.TURN_START) {
			colonized = false;
			addMaterials(income);
		}
	}

	@Deprecated
	public int getTotalMaterials() {
		return resources.get(Resources.MATERIALS).value;
	}

	public void addMaterials(int amount) {
		resources.get(Resources.MATERIALS).value += amount;
	}

	public void removeBuilding(Building building) {
		buildings.remove(building);

		if (building instanceof MineBuilding provider) {
			this.income -= provider.getIncome();
		}
	}

	public void addBuilding(Building building) {
		buildings.add(building);

		if (building instanceof MineBuilding provider) {
			this.income += provider.getIncome();
		}
	}

	public Building getCapitol() {
		return buildings.stream().filter(building -> building instanceof CapitolBuilding).findAny().orElse(null);
	}

	public MutableInt getResource(Resource resource) {
		return resources.get(resource);
	}

	public void addResource(Resource.Quantified resource) {
		getResource(resource.resource()).addNonNegative(resource.quantity());
	}

	public boolean hasResource(Resource.Quantified resource) {
		return getResource(resource.resource()).value >= resource.quantity();
	}

	// TODO make better
	public void removeResource(Resource.Quantified resource) {
		addResource(resource.negate());
	}

}
