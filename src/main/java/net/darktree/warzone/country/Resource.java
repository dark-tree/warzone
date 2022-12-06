package net.darktree.warzone.country;

import net.darktree.warzone.Registries;
import net.darktree.warzone.util.ElementType;
import net.darktree.warzone.util.Registry;

public final class Resource extends ElementType<Resource> {

	private final String shortName;
	private final String longName;

	public Resource(String shortName, String longName) {
		this.shortName = shortName;
		this.longName = longName;
	}

	@Override
	public Registry<Resource> getRegistry() {
		return Registries.RESOURCES;
	}

	public String getShortName() {
		return shortName;
	}

	public String getLongName() {
		return longName;
	}

	public Quantified quantify(int quantity) {
		return new Quantified(this, quantity);
	}

	public record Quantified(Resource resource, int quantity) {

		@Override
		public String toString() {
			return quantity + resource.getShortName();
		}

	}

}
