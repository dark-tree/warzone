package net.darktree.warzone.network.packet;

import net.darktree.warzone.network.Relay;
import net.darktree.warzone.network.VoidPacket;
import net.darktree.warzone.world.WorldHolder;

import java.nio.ByteBuffer;

public class H2CEndTurnPacket extends VoidPacket {

	@Override
	public void onVoidReceive(Relay relay, ByteBuffer buffer) {
		WorldHolder.world.nextPlayerTurn();
	}

	public void send(Relay relay) {
		ByteBuffer buffer = getBuffer();
		relay.broadcastMessage(buffer);
	}

}
