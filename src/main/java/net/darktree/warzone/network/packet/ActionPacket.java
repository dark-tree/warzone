package net.darktree.warzone.network.packet;

import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.network.PacketContext;
import net.darktree.warzone.network.Packets;
import net.darktree.warzone.network.SimplePacket;
import net.darktree.warzone.util.NBTHelper;
import net.darktree.warzone.world.action.manager.Action;
import net.querz.nbt.tag.CompoundTag;

import java.nio.ByteBuffer;

public class ActionPacket extends SimplePacket {

	private final Action action;
	private final Symbol symbol;

	public ActionPacket(ByteBuffer buffer, PacketContext context) {
		super(Packets.ACTION);

		this.symbol = Symbol.fromIndex(buffer.get());
		this.action = Action.fromNbt(NBTHelper.readCompound(buffer), context.getWorld());
	}

	public ActionPacket(Symbol symbol, Action action) {
		super(Packets.ACTION);

		this.action = action;
		this.symbol = symbol;
	}

	@Override
	public void apply(PacketContext context) {
		context.getWorld().getManager().apply(symbol, action, true);
	}

	@Override
	public ByteBuffer getBuffer() {
		ByteBuffer buffer = super.getBuffer();

		buffer.put((byte) symbol.ordinal());
		CompoundTag nbt = new CompoundTag();
		action.toNbt(nbt);
		NBTHelper.writeCompound(nbt, buffer);

		return buffer;
	}

}
