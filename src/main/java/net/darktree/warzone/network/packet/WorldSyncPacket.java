package net.darktree.warzone.network.packet;

import net.darktree.warzone.network.Packets;
import net.darktree.warzone.network.Relay;
import net.darktree.warzone.network.Side;
import net.darktree.warzone.network.SimplePacket;
import net.darktree.warzone.util.Logger;
import net.darktree.warzone.util.NBTHelper;
import net.darktree.warzone.world.World;
import net.darktree.warzone.world.WorldHolder;
import net.querz.nbt.tag.CompoundTag;

import java.nio.ByteBuffer;

public class WorldSyncPacket extends SimplePacket {

	private final CompoundTag nbt;

	public WorldSyncPacket(ByteBuffer buffer, Side side, Relay relay) {
		super(Packets.WORLD);
		side.expect(Side.CLIENT);
		this.nbt = NBTHelper.readCompound(buffer);
	}

	public WorldSyncPacket(World world) {
		super(Packets.WORLD);
		this.nbt = new CompoundTag();
		world.toNbt(this.nbt);
	}

	@Override
	public void apply() {
		WorldHolder.world.fromNbt(this.nbt);
	}

	@Override
	public ByteBuffer getBuffer() {
		ByteBuffer buffer = super.getBuffer();
		NBTHelper.writeCompound(this.nbt, buffer);
		Logger.info("World sync buffer size: ", buffer.position());
		return buffer;
	}

}
