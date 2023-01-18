package net.darktree.warzone.country;

import net.darktree.warzone.Registries;
import net.darktree.warzone.country.storage.Storage;
import net.darktree.warzone.country.storage.StorageSupplier;
import net.darktree.warzone.util.ElementType;
import net.darktree.warzone.util.Registry;

public class Resource extends ElementType<Resource> {

	private final String shortName;
	private final StorageSupplier storage;

	public Resource(String shortName, StorageSupplier storage) {
		this.shortName = shortName;
		this.storage = storage;
	}

	@Override
	public Registry<Resource> getRegistry() {
		return Registries.RESOURCES;
	}

	public String getShortName() {
		return shortName;
	}

	public String getNameKey() {
		return "resource." + key();
	}

	public Quantified quantify(int quantity) {
		return new Quantified(this, quantity);
	}

	public Storage createStorage(Country country) {
		return storage.get(this, country);
	}

	public record Quantified(Resource resource, int quantity) {

		@Override
		public String toString() {
			return quantity + resource.getShortName();
		}

		public Quantified negate() {
			return new Quantified(resource, -quantity);
		}

	}

}
