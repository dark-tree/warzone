package net.darktree.warzone.network.packet;

import net.darktree.warzone.network.PacketContext;
import net.darktree.warzone.network.Packets;
import net.darktree.warzone.network.SimplePacket;

import java.nio.ByteBuffer;

public class EndTurnPacket extends SimplePacket {

	public EndTurnPacket(ByteBuffer buffer, PacketContext context) {
		super(Packets.END_TURN);
	}

	public EndTurnPacket() {
		super(Packets.END_TURN);
	}

	@Override
	public void apply(PacketContext context) {
		context.getWorld().nextPlayerTurn();
	}

}
