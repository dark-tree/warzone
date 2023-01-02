package net.darktree.warzone.country.storage;

import net.darktree.warzone.country.Resource;
import net.darktree.warzone.util.NbtSerializable;
import net.querz.nbt.tag.CompoundTag;
import org.jetbrains.annotations.NotNull;

import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

public class StorageStack implements NbtSerializable, StorageNodeSupplier {

	private final Map<Resource, StorageNode> nodes = new IdentityHashMap<>();

	public void addResourceNode(Resource resource, int limit) {
		nodes.put(resource, new StorageNode(resource, limit));
	}

	public StorageNode getResource(Resource resource) {
		return nodes.get(resource);
	}

	@Override
	public void toNbt(@NotNull CompoundTag tag) {
		nodes.values().forEach(node -> node.toNbt(tag));
	}

	@Override
	public void fromNbt(@NotNull CompoundTag tag) {
		nodes.values().forEach(node -> node.fromNbt(tag));
	}

	@Override
	public void appendStorageNodes(Resource resource, List<StorageNode> list) {
		final StorageNode node = getResource(resource);

		if (node != null) {
			list.add(node);
		}
	}

}
