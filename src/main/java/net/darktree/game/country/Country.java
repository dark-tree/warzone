package net.darktree.game.country;

import net.darktree.core.util.NbtSerializable;
import net.darktree.game.buildings.Building;
import net.querz.nbt.tag.CompoundTag;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Country implements NbtSerializable {

	private final List<Building> buildings = new ArrayList<>();
	private final Symbol symbol;
	private int local = 0;

	public Country(Symbol symbol) {
		this.symbol = symbol;
	}

	@Override
	public void toNbt(@NotNull CompoundTag tag) {
		tag.putByte("symbol", (byte) symbol.ordinal());
	}

	@Override
	public void fromNbt(@NotNull CompoundTag tag) {

	}

	public int getTotalMaterials() {
		return buildings.stream().map(Building::getStored).reduce(local, Integer::sum);
	}

	public void addMaterials(int amount) {
		local += amount;
	}

	public void removeBuilding(Building building) {
		buildings.remove(building);
	}

	public void addBuilding(Building building) {
		buildings.add(building);
	}
}
