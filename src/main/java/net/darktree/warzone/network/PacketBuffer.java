package net.darktree.warzone.network;

import net.darktree.warzone.util.ElementType;
import net.darktree.warzone.util.NbtHelper;
import net.darktree.warzone.util.Registry;
import net.querz.nbt.tag.CompoundTag;

import java.nio.ByteBuffer;

public record PacketBuffer(ByteBuffer buffer) {

	public int size() {
		return buffer.position();
	}

	public void putBool(boolean value) {
		buffer.put((byte) (value ? 1 : 0));
	}

	public boolean getBool() {
		return buffer.get() != 0;
	}

	public void putByte(byte value) {
		buffer.put(value);
	}

	public int getByte() {
		return buffer.get();
	}

	public void putShort(short value) {
		buffer.putShort(value);
	}

	public int getShort() {
		return buffer.getShort();
	}

	public void putInt(int value) {
		buffer.putInt(value);
	}

	public int getInt() {
		return buffer.getInt();
	}

	public void putLong(long value) {
		buffer.putLong(value);
	}

	public long getLong() {
		return buffer.getLong();
	}

	public void putNbt(CompoundTag nbt) {
		NbtHelper.writeCompound(buffer, nbt);
	}

	public CompoundTag getNbt() {
		return NbtHelper.readCompound(buffer);
	}

	public <T extends Enum<T>> void putEnum(Enum<T> value) {
		putShort((short) value.ordinal());
	}

	public <T extends Enum<T>> T getEnum(Class<T> clazz) {
		return clazz.getEnumConstants()[getShort()];
	}

	public <T extends ElementType<T>> void putElement(ElementType<T> value) {
		putInt(value.id());
	}

	public <T extends ElementType<T>> T getElement(Registry<T> registry) {
		return registry.byId(getInt()).value();
	}

}
