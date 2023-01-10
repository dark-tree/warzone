package net.darktree.warzone.country.storage;

import net.darktree.warzone.country.Country;
import net.darktree.warzone.country.Resource;
import net.darktree.warzone.country.upgrade.Upgrades;

public class LocalStorageNode extends StorageNode {

	private final Country country;

	public LocalStorageNode(Resource resource, Country country) {
		super(resource);
		this.country = country;
	}

	@Override
	public int limit() {
		return country.upgrades.get(Upgrades.LOCAL);
	}

}
