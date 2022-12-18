package net.darktree.warzone.network.packet;

import net.darktree.warzone.network.Packets;
import net.darktree.warzone.network.Relay;
import net.darktree.warzone.network.Side;
import net.darktree.warzone.network.SimplePacket;
import net.darktree.warzone.util.Logger;
import net.darktree.warzone.util.NBTHelper;
import net.darktree.warzone.world.WorldHolder;
import net.darktree.warzone.world.entity.building.ProducingBuilding;
import net.darktree.warzone.world.entity.building.production.ProductionState;
import net.querz.nbt.tag.CompoundTag;

import java.nio.ByteBuffer;

public class ProductionSyncPacket extends SimplePacket {

	private final int x;
	private final int y;
	private final CompoundTag nbt;
	private final Side side;

	public ProductionSyncPacket(ByteBuffer buffer, Side side, Relay relay) {
		super(Packets.PRODUCE);

		this.x = buffer.getInt();
		this.y = buffer.getInt();
		this.nbt = NBTHelper.readCompound(buffer);
		this.side = side;
	}

	public ProductionSyncPacket(int x, int y, ProductionState production) {
		super(Packets.PRODUCE);

		this.x = x;
		this.y = y;
		this.nbt = new CompoundTag();
		this.side = null;

		production.toNbt(this.nbt);
	}

	public void apply() {
		Logger.info("Production state received and synced!");
		WorldHolder.world.getEntity(x, y, ProducingBuilding.class).update(nbt);

		if (side == Side.HOST) {
			broadcastExceptHost();
		}
	}

	protected ByteBuffer getBuffer() {
		ByteBuffer buffer = super.getBuffer();

		buffer.putInt(x);
		buffer.putInt(y);
		NBTHelper.writeCompound(nbt, buffer);

		return buffer;
	}

}
