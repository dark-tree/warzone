package net.darktree.warzone.network.packet;

import net.darktree.warzone.network.Packet;
import net.darktree.warzone.network.Relay;
import net.darktree.warzone.world.WorldHolder;

import java.nio.ByteBuffer;

public class H2CEndTurnPacket extends Packet {

	@Override
	public void onReceive(ByteBuffer buffer) {
		WorldHolder.world.nextPlayerTurn();
	}

	public void send(Relay relay) {
		ByteBuffer buffer = getBuffer();
		relay.broadcastMessage(buffer);
	}

}
