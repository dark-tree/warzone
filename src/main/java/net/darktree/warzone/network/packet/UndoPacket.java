package net.darktree.warzone.network.packet;

import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.network.Packets;
import net.darktree.warzone.network.Side;
import net.darktree.warzone.network.VoidPacket;
import net.darktree.warzone.world.WorldHolder;

import java.nio.ByteBuffer;

public class UndoPacket extends VoidPacket {

	private final Symbol symbol;

	public UndoPacket(Side side, ByteBuffer buffer) {
		super(Packets.UNDO);
		this.symbol = Symbol.fromIndex(buffer.get());
	}

	public UndoPacket(Symbol symbol) {
		super(Packets.UNDO);
		this.symbol = symbol;
	}

	@Override
	public void apply() {
		WorldHolder.world.getManager().undo(symbol, true);
	}

	@Override
	public ByteBuffer getBuffer() {
		ByteBuffer buffer = super.getBuffer();
		buffer.put((byte) symbol.ordinal());
		return buffer;
	}

}