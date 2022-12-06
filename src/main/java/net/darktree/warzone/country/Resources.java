package net.darktree.warzone.country;

import net.darktree.warzone.Registries;

public class Resources {
	public static final Resource MATERIALS = Registries.RESOURCES.register("material", new Resource("m", "MATERIALS"));
	public static final Resource ARMOR = Registries.RESOURCES.register("armor", new LabeledResource("z", "ARMOR"));
	public static final Resource AMMO = Registries.RESOURCES.register("ammo", new LabeledResource("a", "AMMUNITION"));
}
