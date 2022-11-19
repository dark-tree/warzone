package net.darktree.warzone.network;

import net.darktree.warzone.Registries;
import net.darktree.warzone.network.packet.ActionPacket;
import net.darktree.warzone.network.packet.EndTurnPacket;
import net.darktree.warzone.network.packet.GroupSyncPacket;

public class Packets {

	public static final EndTurnPacket NEXT_TURN_PACKET = new EndTurnPacket();
	public static final GroupSyncPacket GROUP_SYNC = new GroupSyncPacket();
	public static final ActionPacket H2C_ACTION = new ActionPacket();

	public static void load() {
		Registries.PACKETS.register("test", NEXT_TURN_PACKET);
		Registries.PACKETS.register("group_sync", GROUP_SYNC);
		Registries.PACKETS.register("action", H2C_ACTION);
	}

}
