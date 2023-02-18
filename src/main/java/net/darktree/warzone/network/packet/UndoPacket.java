package net.darktree.warzone.network.packet;

import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.network.PacketBuffer;
import net.darktree.warzone.network.PacketContext;
import net.darktree.warzone.network.Packets;
import net.darktree.warzone.network.SimplePacket;

public class UndoPacket extends SimplePacket {

	private final Symbol symbol;

	public UndoPacket(PacketBuffer buffer, PacketContext context) {
		super(Packets.UNDO);
		this.symbol = buffer.getEnum(Symbol.class);
	}

	public UndoPacket(Symbol symbol) {
		super(Packets.UNDO);
		this.symbol = symbol;
	}

	@Override
	public void apply(PacketContext context) {
		context.getWorld().getManager().undo(symbol, true);
	}

	@Override
	public PacketBuffer getBuffer() {
		PacketBuffer buffer = super.getBuffer();
		buffer.putEnum(symbol);
		return buffer;
	}

}