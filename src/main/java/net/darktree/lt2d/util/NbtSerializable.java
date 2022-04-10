package net.darktree.lt2d.util;

import net.querz.nbt.tag.CompoundTag;
import org.jetbrains.annotations.NotNull;

public interface NbtSerializable {
	void toNbt(@NotNull CompoundTag tag);
	void fromNbt(@NotNull CompoundTag tag);
}
