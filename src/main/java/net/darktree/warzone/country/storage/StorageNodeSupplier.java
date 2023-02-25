package net.darktree.warzone.country.storage;

import net.darktree.warzone.country.Resource;

import java.util.List;

public interface StorageNodeSupplier {

	/**
	 * Append all {@link StorageNode}s corresponding to the given {@link Resource},
	 * this will be called on all implementing buildings a country has when a resource
	 * uses {@link DistributedStorage}.
	 */
	void appendStorageNodes(Resource resource, List<StorageNode> list);

}
