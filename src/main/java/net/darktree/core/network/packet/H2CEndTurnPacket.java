package net.darktree.core.network.packet;

import net.darktree.core.network.Packet;
import net.darktree.core.network.Relay;
import net.darktree.core.world.WorldHolder;

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
