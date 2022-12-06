package net.darktree.warzone.country;

import net.darktree.warzone.Registries;

public class Resources {
	public static final Resource MATERIALS = Registries.RESOURCES.register("material", new Resource("M", "materials"));
	public static final Resource ARMOR = Registries.RESOURCES.register("armor", new Resource("Z", "armor"));
	public static final Resource AMMO = Registries.RESOURCES.register("ammo", new Resource("A", "ammunition"));
}
