package net.darktree.warzone.country.storage;

import net.darktree.warzone.country.Resource;

import java.util.List;
import java.util.function.Function;

public class DistributedStorage extends Storage {

	private final Function<Resource, List<StorageNode>> nodes;
	private final Resource resource;

	public DistributedStorage(Resource resource, Function<Resource, List<StorageNode>> nodes) {
		this.nodes = nodes;
		this.resource = resource;
	}

	private List<StorageNode> getNodes() {
		return nodes.apply(resource);
	}

	@Override
	public int getMaxValue() {
		int value = 0;

		for (StorageNode node : getNodes()) {
			value += node.limit();
		}

		return value;
	}

	@Override
	public int get() {
		int value = 0;

		for (StorageNode node : getNodes()) {
			value += node.amount();
		}

		return value;
	}

	@Override
	public void set(int amount) {
		add(amount - get());
	}

	@Override
	public void add(int amount) {
		for (StorageNode node : getNodes()) {
			amount = node.insert(amount);

			if (amount == 0) {
				return;
			}
		}

		// if we reached this point some resources
		// did not fit into the distributed storage
	}

	@Override
	public boolean serialize() {
		return false;
	}

}
