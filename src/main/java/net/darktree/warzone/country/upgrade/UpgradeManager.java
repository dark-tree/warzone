package net.darktree.warzone.country.upgrade;

import net.darktree.warzone.Registries;
import net.darktree.warzone.util.NbtHelper;
import net.darktree.warzone.util.NbtSerializable;
import net.querz.nbt.tag.CompoundTag;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.Supplier;

public class UpgradeManager implements NbtSerializable {

	private final Map<Upgrade<?>, UpgradeState> upgrades;
	private final Supplier<Boolean> activator;

	public UpgradeManager(Supplier<Boolean> activator) {
		this.upgrades = Registries.UPGRADES.map(upgrade -> new UpgradeState(false));
		this.activator = activator;
	}

	public <T> boolean isEnabled(Upgrade<T> upgrade) {
		return upgrades.get(upgrade).value;
	}

	public <T> boolean isActive(Upgrade<T> upgrade) {
		return activator.get() && isEnabled(upgrade);
	}

	public <T> T get(Upgrade<T> upgrade) {
		return upgrade.get(isActive(upgrade));
	}

	public <T> void grant(Upgrade<T> upgrade) {
		upgrades.get(upgrade).grant();
	}

	@Override
	public void toNbt(@NotNull CompoundTag nbt) {
		CompoundTag upgradesTag = NbtHelper.getTag("upgrades", nbt);

		upgrades.forEach((upgrade, state) -> {
			upgradesTag.putBoolean(upgrade.key(), state.value);
		});
	}

	@Override
	public void fromNbt(@NotNull CompoundTag nbt) {
		CompoundTag upgradesTag = NbtHelper.getTag("upgrades", nbt);

		upgrades.forEach((upgrade, state) -> {
			state.value = upgradesTag.getBoolean(upgrade.key());
		});
	}

}
