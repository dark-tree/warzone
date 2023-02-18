package net.darktree.warzone.util;

import net.querz.nbt.io.NBTDeserializer;
import net.querz.nbt.io.NBTSerializer;
import net.querz.nbt.io.NBTUtil;
import net.querz.nbt.io.NamedTag;
import net.querz.nbt.tag.CompoundTag;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

public class NbtHelper {

	private static final NBTSerializer SERIALIZER = new NBTSerializer(true);
	private static final NBTDeserializer DESERIALIZER = new NBTDeserializer(true);

	/**
	 * Write a NBT Compound Tag into a ByteBuffer
	 */
	public static void writeCompound(ByteBuffer buffer, CompoundTag nbt) {
		try {
			SERIALIZER.toStream(new NamedTag(null, nbt), new ByteBufferOutputStream(buffer));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Read a NBT Compound Tag from a ByteBuffer
	 */
	public static CompoundTag readCompound(ByteBuffer buffer) {
		try {
			return (CompoundTag) DESERIALIZER.fromStream(new ByteBufferInputStream(buffer)).getTag();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Read a NBT Compound Tag from a compressed file
	 */
	public static CompoundTag readFile(File file) {
		try {
			return (CompoundTag) NBTUtil.read(file, true).getTag();
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Write a NBT Compound Tag to a compressed file
	 */
	public static boolean writeFile(File file, CompoundTag nbt) {
		try {
			NBTUtil.write(nbt, file, true);
		} catch (Exception e) {
			return false;
		}

		return true;
	}

	/**
	 * Get a tag from tag, guarantees that the returned value will be a non-null
	 * CompoundTag, and that this CompoundTag will be a member of given NBT object
	 */
	public static CompoundTag getTag(String name, CompoundTag nbt) {
		CompoundTag child = nbt.getCompoundTag(name);

		if (child == null) {
			child = new CompoundTag();
			nbt.put(name, child);
		}

		return child;
	}

}
