package net.darktree.warzone.country;

import net.darktree.warzone.Registries;
import net.darktree.warzone.country.controller.NullController;
import net.darktree.warzone.country.storage.LocalStorageNode;
import net.darktree.warzone.country.storage.Storage;
import net.darktree.warzone.country.storage.StorageNode;
import net.darktree.warzone.country.storage.StorageNodeSupplier;
import net.darktree.warzone.country.upgrade.UpgradeManager;
import net.darktree.warzone.country.upgrade.Upgrades;
import net.darktree.warzone.util.NbtHelper;
import net.darktree.warzone.util.NbtSerializable;
import net.darktree.warzone.world.TurnEvent;
import net.darktree.warzone.world.World;
import net.darktree.warzone.world.WorldListener;
import net.darktree.warzone.world.entity.building.*;
import net.querz.nbt.tag.CompoundTag;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

public class Country implements NbtSerializable, WorldListener {

	public final Symbol symbol;
	public final UpgradeManager upgrades = new UpgradeManager(this::hasParliament);
	private final World world;
	private final List<Building> buildings = new ArrayList<>();
	private final List<StorageNodeSupplier> storages = new ArrayList<>();
	private final Map<Resource, Storage> resources;
	private final StorageNode local;

	public Controller controller = new NullController();
	private int colonizations = 0;
	private int parliaments = 0;
	private int income = 0;

	public Country(Symbol symbol, World world) {
		this.symbol = symbol;
		this.world = world;
		this.resources = Registries.RESOURCES.map(new IdentityHashMap<>(), resource -> resource.createStorage(this));
		this.local = new LocalStorageNode(Resources.MATERIALS, this);
	}

	@Override
	public void toNbt(@NotNull CompoundTag tag) {
		tag.putByte("symbol", (byte) symbol.ordinal());

		CompoundTag resourcesTag = NbtHelper.getTag("resources", tag);
		resources.forEach((resource, entry) -> {
			if (entry.serialize()) resourcesTag.putInt(resource.key(), entry.get());
		});

		local.toNbt(tag);
		upgrades.toNbt(tag);
	}

	@Override
	public void fromNbt(@NotNull CompoundTag tag) {
		CompoundTag resourcesTag = NbtHelper.getTag("resources", tag);
		resources.forEach((resource, entry) -> {
			if (entry.serialize()) entry.set(resourcesTag.getInt(resource.key()));
		});

		local.fromNbt(tag);
		upgrades.fromNbt(tag);
	}

	@Override
	public void onPlayerTurnEvent(TurnEvent event, Symbol symbol) {
		if (symbol == this.symbol && event == TurnEvent.TURN_START) {
			colonizations = 0;
			addMaterials(income);
			controller.turnStart(this, world);
		}
	}

	public boolean hasParliament() {
		return parliaments > 0;
	}

	@Deprecated
	public int getTotalMaterials() {
		return resources.get(Resources.MATERIALS).get();
	}

	@Deprecated
	public void addMaterials(int amount) {
		resources.get(Resources.MATERIALS).add(amount);
	}

	public void removeBuilding(Building building) {
		buildings.remove(building);

		if (building instanceof MineBuilding mine) {
			this.income -= mine.getIncome();
		}

		if (building instanceof WarehouseBuilding warehouse) {
			this.storages.remove(warehouse);
		}

		if (building instanceof ParliamentBuilding) {
			parliaments --;
		}
	}

	public void addBuilding(Building building) {
		buildings.add(building);

		if (building instanceof MineBuilding mine) {
			this.income += mine.getIncome();
		}

		if (building instanceof WarehouseBuilding warehouse) {
			this.storages.add(warehouse);
		}

		if (building instanceof ParliamentBuilding) {
			parliaments ++;
		}
	}

	public CapitolBuilding getCapitol() {
		return (CapitolBuilding) buildings.stream().filter(building -> building instanceof CapitolBuilding).findAny().orElse(null);
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

	public float getRelationWith(Symbol symbol) {
		return 0;
	}

	public int getLocalMaterials() {
		return local.amount();
	}

	public int getIncome() {
		return income;
	}

	public boolean canColonize() {
		return colonizations < upgrades.get(Upgrades.DOUBLE);
	}

	public void onColonize() {
		colonizations ++;
	}

	public Controller getController() {
		return controller;
	}

}
