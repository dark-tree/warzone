package net.darktree.warzone.util;

import net.querz.nbt.io.NBTUtil;
import net.querz.nbt.tag.CompoundTag;

import java.io.File;
import java.util.Optional;

public class NbtAccess {

	public static Optional<CompoundTag> tag(String name, CompoundTag nbt) {
		return Optional.ofNullable(nbt.getCompoundTag(name));
	}

	public static CompoundTag getTag(String name, CompoundTag nbt) {
		return tag(name, nbt).orElseGet(() -> {
			CompoundTag tag = new CompoundTag();
			nbt.put(name, tag);
			return tag;
		});
	}

	public static CompoundTag readFile(File file) {
		try {
			return (CompoundTag) NBTUtil.read(file, true).getTag();
		} catch (Exception e) {
			return null;
		}
	}

}
