package net.darktree.warzone.country;

import net.darktree.warzone.Registries;
import net.darktree.warzone.country.storage.Storage;

public class Resources {
	public static final Resource MATERIALS = Registries.RESOURCES.register("material", new Resource("m", "MATERIALS", Storage::distributed));
	public static final Resource ARMOR = Registries.RESOURCES.register("armor", new LabeledResource("z", "ARMOR", Storage::simple));
	public static final Resource AMMO = Registries.RESOURCES.register("ammo", new LabeledResource("a", "AMMUNITION", Storage::simple));
}
