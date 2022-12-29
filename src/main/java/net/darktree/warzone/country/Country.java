package net.darktree.warzone.country;

import net.darktree.warzone.Registries;
import net.darktree.warzone.country.storage.Storage;
import net.darktree.warzone.country.storage.StorageNode;
import net.darktree.warzone.country.storage.StorageNodeSupplier;
import net.darktree.warzone.event.TurnEvent;
import net.darktree.warzone.util.NbtSerializable;
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
	private final List<StorageNodeSupplier> storages = new ArrayList<>();
	public final Symbol symbol;
	public boolean colonized = false;

	public int income = 0;

	private final Map<Resource, Storage> resources;
	private final StorageNode local;

	public Country(Symbol symbol) {
		this.symbol = symbol;
		this.resources = Registries.RESOURCES.map(new IdentityHashMap<>(), resource -> resource.createStorage(this));
		this.local = new StorageNode(Resources.MATERIALS, Storage.SMALL);
	}

	@Override
	public void toNbt(@NotNull CompoundTag tag) {
		tag.putByte("symbol", (byte) symbol.ordinal());
		local.toNbt(tag);
		resources.forEach((resource, entry) -> {
			if (entry.serialize()) tag.putInt(resource.key(), entry.get());
		});
	}

	@Override
	public void fromNbt(@NotNull CompoundTag tag) {
		local.fromNbt(tag);
		resources.forEach((resource, entry) -> {
			if (entry.serialize()) entry.set(tag.getInt(resource.key()));
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
		return resources.get(Resources.MATERIALS).get();
	}

	public void addMaterials(int amount) {
		resources.get(Resources.MATERIALS).add(amount);
	}

	public void removeBuilding(Building building) {
		buildings.remove(building);

		if (building instanceof MineBuilding provider) {
			this.income -= provider.getIncome();
		}

		if (building instanceof StorageNodeSupplier storage) {
			this.storages.remove(storage);
		}
	}

	public void addBuilding(Building building) {
		buildings.add(building);

		if (building instanceof MineBuilding provider) {
			this.income += provider.getIncome();
		}

		if (building instanceof StorageNodeSupplier storage) {
			this.storages.add(storage);
		}
	}

	public Building getCapitol() {
		return buildings.stream().filter(building -> building instanceof CapitolBuilding).findAny().orElse(null);
	}

	public Storage getResource(Resource resource) {
		return resources.get(resource);
	}

	public void addResource(Resource.Quantified resource) {
		getResource(resource.resource()).add(resource.quantity());
	}

	public boolean hasResource(Resource.Quantified resource) {
		return getResource(resource.resource()).has(resource.quantity());
	}

	public void removeResource(Resource.Quantified resource) {
		addResource(resource.negate());
	}

	public List<StorageNode> getStorageNodes(Resource resource) {
		List<StorageNode> nodes = new ArrayList<>();

		if (resource == Resources.MATERIALS) {
			nodes.add(local);
		}

		for (StorageNodeSupplier storage : this.storages) {
			storage.appendStorageNodes(resource, nodes);
		}

		return nodes;
	}

	public int getLocalMaterials() {
		return local.amount();
	}

}
