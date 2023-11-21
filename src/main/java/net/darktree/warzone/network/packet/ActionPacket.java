package net.darktree.warzone.network.packet;

import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.network.PacketBuffer;
import net.darktree.warzone.network.PacketContext;
import net.darktree.warzone.network.Packets;
import net.darktree.warzone.network.SimplePacket;
import net.darktree.warzone.world.action.ledger.Action;
import net.querz.nbt.tag.CompoundTag;

public class ActionPacket extends SimplePacket {

	private final Action action;
	private final Symbol symbol;

	public ActionPacket(PacketBuffer buffer, PacketContext context) {
		super(Packets.ACTION);

		this.symbol = buffer.getEnum(Symbol.class);
		this.action = Action.load(buffer.getNbt());
	}

	public ActionPacket(Symbol symbol, Action action) {
		super(Packets.ACTION);

		this.action = action;
		this.symbol = symbol;
	}

	@Override
	public void apply(PacketContext context) {
		context.getWorld().getLedger().push(action, true);
	}

	@Override
	public PacketBuffer getBuffer() {
		PacketBuffer buffer = super.getBuffer();

		CompoundTag nbt = new CompoundTag();
		action.toNbt(nbt);

		buffer.putEnum(symbol);
		buffer.putNbt(nbt);

		return buffer;
	}

}
