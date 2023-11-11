package net.darktree.warzone.country.storage;

import net.darktree.warzone.country.Resource;
import net.darktree.warzone.util.NbtSerializable;
import net.darktree.warzone.util.math.MathHelper;
import net.querz.nbt.tag.CompoundTag;
import org.jetbrains.annotations.NotNull;

public abstract class StorageNode implements NbtSerializable {

	private final Resource resource;
	private int amount;

	public StorageNode(Resource resource) {
		this.resource = resource;
		this.amount = 0;
	}

	@Override
	public void toNbt(@NotNull CompoundTag tag) {
		tag.putInt(resource.key(), amount);
	}

	@Override
	public void fromNbt(@NotNull CompoundTag tag) {
		amount = MathHelper.clamp(tag.getInt(resource.key()), 0, limit());
	}

	public Resource getResource() {
		return resource;
	}

	public int amount() {
		return amount;
	}

	public abstract int limit();

	public int insert(int amount) {
		this.amount += amount;
		int current = this.amount;
		int max = limit();

		if (current < 0) {
			this.amount = 0;
			return current;
		}

		if (current > max) {
			this.amount = max;
			return current - max;
		}

		return 0;
	}

}
