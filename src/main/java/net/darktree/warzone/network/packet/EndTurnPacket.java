package net.darktree.warzone.network.packet;

import net.darktree.warzone.network.Packets;
import net.darktree.warzone.network.Relay;
import net.darktree.warzone.network.Side;
import net.darktree.warzone.network.SimplePacket;
import net.darktree.warzone.world.WorldHolder;

import java.nio.ByteBuffer;

public class EndTurnPacket extends SimplePacket {

	public EndTurnPacket(ByteBuffer buffer, Side side, Relay relay) {
		super(Packets.END_TURN);
	}

	public EndTurnPacket() {
		super(Packets.END_TURN);
	}

	@Override
	public void apply() {
		WorldHolder.world.nextPlayerTurn();
	}

}
