package net.darktree.warzone.country.storage;

import net.darktree.warzone.country.Resource;

public class LimitedStorageNode extends StorageNode {

	private final int limit;

	public LimitedStorageNode(Resource resource, int limit) {
		super(resource);
		this.limit = limit;
	}

	@Override
	public int limit() {
		return limit;
	}

}
