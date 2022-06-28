package net.darktree.core.network;

import net.darktree.core.Registries;
import net.darktree.core.network.packet.H2CEndTurnPacket;

public class Packets {

	public static final H2CEndTurnPacket NEXT_TURN_PACKET = new H2CEndTurnPacket();

	public static void load() {
		Registries.PACKETS.register("test", NEXT_TURN_PACKET);
	}

}
