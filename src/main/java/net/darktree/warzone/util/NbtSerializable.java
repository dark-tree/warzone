package net.darktree.warzone.util;

import net.querz.nbt.tag.CompoundTag;
import org.jetbrains.annotations.NotNull;

public interface NbtSerializable {
	void toNbt(@NotNull CompoundTag nbt);
	void fromNbt(@NotNull CompoundTag nbt);
}
