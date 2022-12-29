package net.darktree.warzone.country.storage;

import net.darktree.warzone.country.Resource;

import java.util.List;

public interface StorageNodeSupplier {

	void appendStorageNodes(Resource resource, List<StorageNode> list);

}
