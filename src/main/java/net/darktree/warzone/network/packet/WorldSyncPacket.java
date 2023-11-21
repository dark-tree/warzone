package net.darktree.warzone.network.packet;

import net.darktree.warzone.network.*;
import net.darktree.warzone.util.Logger;
import net.darktree.warzone.world.WorldSnapshot;
import net.querz.nbt.tag.CompoundTag;

public class WorldSyncPacket extends SimplePacket {

	private final CompoundTag nbt;

	public WorldSyncPacket(PacketBuffer buffer, PacketContext context) {
		super(Packets.WORLD);
		context.expect(Side.CLIENT);
		this.nbt = buffer.getNbt();
	}

	public WorldSyncPacket(WorldSnapshot world) {
		super(Packets.WORLD);
		this.nbt = new CompoundTag();
		world.toNbt(this.nbt);
	}

	@Override
	public void apply(PacketContext context) {
		context.getWorld().fromNbt(this.nbt);
	}

	@Override
	public PacketBuffer getBuffer() {
		PacketBuffer buffer = super.getBuffer();

		buffer.putNbt(this.nbt);
		Logger.info("World sync buffer size: ", buffer.size());

		return buffer;
	}

}
