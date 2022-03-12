package net.darktree.game.nbt;

import net.querz.nbt.tag.CompoundTag;

public interface NbtSerializable {
	void toNbt(CompoundTag tag);
	void fromNbt(CompoundTag tag);
}
