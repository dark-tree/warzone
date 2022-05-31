package net.darktree.game.country;

import net.darktree.core.event.TurnEvent;
import net.darktree.core.util.NbtSerializable;
import net.darktree.core.world.World;
import net.darktree.core.world.WorldListener;
import net.darktree.game.buildings.Building;
import net.querz.nbt.tag.CompoundTag;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Country implements NbtSerializable, WorldListener {

	private final List<Building> buildings = new ArrayList<>();
	private final Symbol symbol;
	public boolean colonized = false;

	private int local = 0;
	private int ammo = 0;

	public Country(Symbol symbol) {
		this.symbol = symbol;
	}

	@Override
	public void toNbt(@NotNull CompoundTag tag) {
		tag.putByte("symbol", (byte) symbol.ordinal());
		tag.putInt("local", local);
		tag.putInt("ammo", ammo);
	}

	@Override
	public void fromNbt(@NotNull CompoundTag tag) {
		this.local = tag.getInt("local");
		this.ammo = tag.getInt("ammo");
	}

	public void onPlayerTurnEvent(World world, TurnEvent event, Symbol symbol) {
		if (symbol == this.symbol && event == TurnEvent.TURN_START) {
			colonized = false;
		}
	}

	public int getTotalMaterials() {
		return buildings.stream().map(Building::getStored).reduce(local, Integer::sum);
	}

	public void addMaterials(int amount) {
		local += amount;
	}

	public int getAmmo() {
		return ammo;
	}

	public void addAmmo(int amount) {
		ammo += amount;
	}

	public void removeBuilding(Building building) {
		buildings.remove(building);
	}

	public void addBuilding(Building building) {
		buildings.add(building);
	}

	public Building getCapitol() {
		try {
			return buildings.get(0); // FIXME
		}catch (Exception e) {
			return null;
		}
	}

}
