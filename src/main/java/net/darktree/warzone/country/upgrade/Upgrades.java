package net.darktree.warzone.country.upgrade;

import net.darktree.warzone.Registries;
import net.darktree.warzone.client.Sprites;

public class Upgrades {
	public static final Upgrade<Integer> DOUBLE = Registries.UPGRADES.register("double", Upgrade.create(20, Sprites.UPGRADE_DOUBLE).of(1, 2));
	public static final Upgrade<Float> RECYCLE = Registries.UPGRADES.register("recycle", Upgrade.create(10, Sprites.UPGRADE_RECYCLE).of(0.4f, 0.6f));
	public static final Upgrade<Integer> LOCAL = Registries.UPGRADES.register("local", Upgrade.create(5, Sprites.UPGRADE_LOCAL).of(10, 20));
	public static final Upgrade<Integer> MAPS = Registries.UPGRADES.register("maps", Upgrade.create(10, Sprites.UPGRADE_MAPS).of(5, 6));
}
