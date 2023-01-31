package net.darktree.warzone.network.packet;

import net.darktree.warzone.network.PacketContext;
import net.darktree.warzone.network.Packets;
import net.darktree.warzone.network.Side;
import net.darktree.warzone.network.SimplePacket;
import net.darktree.warzone.util.Logger;
import net.darktree.warzone.util.NBTHelper;
import net.darktree.warzone.world.World;
import net.querz.nbt.tag.CompoundTag;

import java.nio.ByteBuffer;

public class WorldSyncPacket extends SimplePacket {

	private final CompoundTag nbt;

	public WorldSyncPacket(ByteBuffer buffer, PacketContext context) {
		super(Packets.WORLD);
		context.expect(Side.CLIENT);
		this.nbt = NBTHelper.readCompound(buffer);
	}

	public WorldSyncPacket(World world) {
		super(Packets.WORLD);
		this.nbt = new CompoundTag();
		world.toNbt(this.nbt);
	}

	@Override
	public void apply(PacketContext context) {
		context.getWorld().fromNbt(this.nbt);
	}

	@Override
	public ByteBuffer getBuffer() {
		ByteBuffer buffer = super.getBuffer();
		NBTHelper.writeCompound(this.nbt, buffer);
		Logger.info("World sync buffer size: ", buffer.position());
		return buffer;
	}

}
