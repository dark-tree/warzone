package net.darktree.warzone.network.packet;

import net.darktree.warzone.Main;
import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.network.PacketDelegate;
import net.darktree.warzone.network.Side;
import net.darktree.warzone.network.VoidPacket;
import net.darktree.warzone.world.WorldHolder;

import java.nio.ByteBuffer;

public class UndoPacket extends VoidPacket {

	@Override
	public void onReceive(Side side, ByteBuffer buffer) {
		Symbol symbol = Symbol.fromIndex(buffer.get());

		Main.runSynced(() -> {
			WorldHolder.world.getManager().undo(symbol, true);
		});
	}

	public PacketDelegate of(Symbol symbol) {
		ByteBuffer buffer = getBuffer();
		buffer.put((byte) symbol.ordinal());
		return new PacketDelegate(buffer);
	}

}