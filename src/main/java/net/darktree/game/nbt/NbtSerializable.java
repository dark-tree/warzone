package net.darktree.game.nbt;

import net.querz.nbt.tag.CompoundTag;
import org.jetbrains.annotations.NotNull;

public interface NbtSerializable {
	void toNbt(@NotNull CompoundTag tag);
	void fromNbt(@NotNull CompoundTag tag);
}
