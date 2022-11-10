package net.darktree.warzone.util;

import net.querz.nbt.io.NBTDeserializer;
import net.querz.nbt.io.NBTSerializer;
import net.querz.nbt.io.NamedTag;
import net.querz.nbt.tag.CompoundTag;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class NBTHelper {

	private static final NBTSerializer SERIALIZER = new NBTSerializer(true);
	private static final NBTDeserializer DESERIALIZER = new NBTDeserializer(true);

	public static void writeCompound(CompoundTag nbt, OutputStream stream) {
		try {
			SERIALIZER.toStream(new NamedTag(null, nbt), stream);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static CompoundTag readCompound(InputStream stream) {
		try {
			return (CompoundTag) DESERIALIZER.fromStream(stream).getTag();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static void writeCompound(CompoundTag nbt, ByteBuffer buffer) {
		writeCompound(nbt, new ByteBufferOutputStream(buffer));
	}

	public static CompoundTag readCompound(ByteBuffer buffer) {
		return readCompound(new ByteBufferInputStream(buffer));
	}

}
