package net.darktree.game.country;

import net.darktree.core.event.TurnEvent;
import net.darktree.core.util.NbtSerializable;
import net.darktree.core.world.World;
import net.darktree.core.world.WorldListener;
import net.darktree.core.world.tile.MaterialProvider;
import net.darktree.game.buildings.Building;
import net.darktree.game.buildings.CapitolBuilding;
import net.querz.nbt.tag.CompoundTag;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Country implements NbtSerializable, WorldListener {

	private final List<Building> buildings = new ArrayList<>();
	private final Symbol symbol;
	public boolean colonized = false;

	private int local = 0;
	public int ammo = 0;
	public int armor = 0;
	public int income = 0;

	public Country(Symbol symbol) {
		this.symbol = symbol;
	}

	@Override
	public void toNbt(@NotNull CompoundTag tag) {
		tag.putByte("symbol", (byte) symbol.ordinal());
		tag.putInt("local", local);
		tag.putInt("ammo", ammo);
		tag.putInt("armor", armor);
	}

	@Override
	public void fromNbt(@NotNull CompoundTag tag) {
		this.local = tag.getInt("local");
		this.ammo = tag.getInt("ammo");
		this.armor = tag.getInt("armor");
	}

	public void onPlayerTurnEvent(World world, TurnEvent event, Symbol symbol) {
		if (symbol == this.symbol && event == TurnEvent.TURN_START) {
			colonized = false;
			local += income;
		}
	}

	public int getTotalMaterials() {
		return buildings.stream().map(Building::getStored).reduce(local, Integer::sum);
	}

	public void addMaterials(int amount) {
		local += amount;
	}

	public void removeBuilding(Building building) {
		buildings.remove(building);

		if (building instanceof MaterialProvider provider) {
			this.income -= provider.getIncome();
		}
	}

	public void addBuilding(Building building) {
		buildings.add(building);

		if (building instanceof MaterialProvider provider) {
			this.income += provider.getIncome();
		}
	}

	public Building getCapitol() {
		return buildings.stream().filter(building -> building instanceof CapitolBuilding).findAny().orElse(null);
	}

	public void addArmor(int i) {
		armor += i;
	}

}
