package net.darktree.warzone.network;

import net.darktree.warzone.Registries;
import net.darktree.warzone.network.packet.H2CActionPacket;
import net.darktree.warzone.network.packet.H2CEndTurnPacket;
import net.darktree.warzone.network.packet.H2CGroupSyncPacket;

public class Packets {

	public static final H2CEndTurnPacket NEXT_TURN_PACKET = new H2CEndTurnPacket();
	public static final H2CGroupSyncPacket GROUP_SYNC = new H2CGroupSyncPacket();
	public static final H2CActionPacket H2C_ACTION = new H2CActionPacket();

	public static void load() {
		Registries.PACKETS.register("test", NEXT_TURN_PACKET);
		Registries.PACKETS.register("group_sync", GROUP_SYNC);
		Registries.PACKETS.register("action", H2C_ACTION);
	}

}
