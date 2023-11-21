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
import net.darktree.warzone.world.WorldListener;
import net.darktree.warzone.world.WorldSnapshot;
import net.darktree.warzone.world.entity.Entity;
import net.darktree.warzone.world.entity.StructureEntity;
import net.darktree.warzone.world.entity.building.Building;
import net.darktree.warzone.world.entity.building.CapitolBuilding;
import net.darktree.warzone.world.entity.building.MineBuilding;
import net.darktree.warzone.world.entity.building.ParliamentBuilding;
import net.querz.nbt.tag.CompoundTag;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class Country implements NbtSerializable, WorldListener {

	public final Symbol symbol;
	public final UpgradeManager upgrades = new UpgradeManager(this::hasParliament);
	private final WorldSnapshot world;
	private final Map<Resource, Storage> resources;
	private final StorageNode local;

	public Controller controller = new NullController();
	private int colonizations = 0;

	public Country(Symbol symbol, WorldSnapshot world) {
		this.symbol = symbol;
		this.world = world;
		this.resources = Registries.RESOURCES.map(resource -> resource.createStorage(this));
		this.local = new LocalStorageNode(Resources.MATERIALS, this);
	}

	public Country copy(WorldSnapshot copy) {
		Country country = new Country(symbol, copy);
		country.colonizations = this.colonizations;
		country.upgrades.copyOf(this.upgrades);
		country.local.set(this.local.amount());
		country.controller = this.controller; // FIXME idk why this doesn't really work :thonk:

		country.resources.forEach((resource, storage) -> {
			storage.set(this.resources.get(resource).get());
		});

		return country;
	}

	public Stream<Entity> getEntities() {
		return world.getEntities().stream().filter(entity -> entity.isOf(this.symbol));
	}

	public Stream<StructureEntity> getStructures() {
		return getEntities().filter(entity -> entity instanceof StructureEntity).map(entity -> (StructureEntity) entity);
	}

	public Stream<Building> getBuildings() {
		return getEntities().filter(entity -> entity instanceof Building).map(entity -> (Building) entity);
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
			addMaterials(getIncome());
			controller.turnStart(this, world);
		}
	}

	@Deprecated
	public int getTotalMaterials() {
		return resources.get(Resources.MATERIALS).get();
	}

	@Deprecated
	public void addMaterials(int amount) {
		resources.get(Resources.MATERIALS).add(amount);
	}

	@Deprecated(forRemoval = true)
	public void removeBuilding(Building building) {

	}

	@Deprecated(forRemoval = true)
	public void addBuilding(Building building) {

	}

	public boolean hasParliament() {
		// TODO: decouple from ParliamentBuilding (?)
		return getEntities().anyMatch(entity -> entity instanceof ParliamentBuilding);
	}

	public int getIncome() {
		// TODO: decouple from MineBuilding (?)
		return getEntities().filter(entity -> entity instanceof MineBuilding).mapToInt(entity -> ((MineBuilding) entity).getIncome()).sum();
	}

	public Stream<StorageNodeSupplier> getBuildingStorageNodes() {
		return getEntities().filter(entity -> entity instanceof StorageNodeSupplier).map(entity -> (StorageNodeSupplier) entity);
	}

	public CapitolBuilding getCapitol() {
		return (CapitolBuilding) getEntities().filter(building -> building instanceof CapitolBuilding).findAny().orElse(null);
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

		getBuildingStorageNodes().forEach(storage -> {
			storage.appendStorageNodes(resource, nodes);
		});

		return nodes;
	}

	public float getRelationWith(Symbol symbol) {
		return 0;
	}

	public int getLocalMaterials() {
		return local.amount();
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
