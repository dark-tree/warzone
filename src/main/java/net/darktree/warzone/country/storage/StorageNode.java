package net.darktree.warzone.country.storage;

import net.darktree.warzone.country.Resource;
import net.darktree.warzone.util.NbtSerializable;
import net.darktree.warzone.util.math.MathHelper;
import net.querz.nbt.tag.CompoundTag;
import org.jetbrains.annotations.NotNull;

public class StorageNode implements NbtSerializable {

	private final Resource resource;
	private int limit;
	private int amount;

	public StorageNode(Resource resource, int limit) {
		this.resource = resource;
		this.limit = limit;
		this.amount = 0;
	}

	@Override
	public void toNbt(@NotNull CompoundTag tag) {
		tag.putInt(resource.key(), amount);
	}

	@Override
	public void fromNbt(@NotNull CompoundTag tag) {
		amount = MathHelper.clamp(tag.getInt(resource.key()), 0, limit);
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int amount() {
		return amount;
	}

	public int limit() {
		return limit;
	}

	public int insert(int amount) {
		this.amount += amount;
		int current = this.amount;

		if (current < 0) {
			this.amount = 0;
			return current;
		}

		if (current > limit) {
			this.amount = limit;
			return current - limit;
		}

		return 0;
	}

}
