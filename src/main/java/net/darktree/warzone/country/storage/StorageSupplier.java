package net.darktree.warzone.country.storage;

import net.darktree.warzone.country.Country;
import net.darktree.warzone.country.Resource;

@FunctionalInterface
public interface StorageSupplier {

	Storage get(Resource resource, Country country);

}
