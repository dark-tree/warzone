package net.darktree.warzone.network;

import net.darktree.warzone.Registries;
import net.darktree.warzone.network.packet.ActionPacket;
import net.darktree.warzone.network.packet.EndTurnPacket;
import net.darktree.warzone.network.packet.GroupSyncPacket;
import net.darktree.warzone.network.packet.UndoPacket;

public class Packets {

	public static final EndTurnPacket END_TURN = new EndTurnPacket();
	public static final GroupSyncPacket GROUP_SYNC = new GroupSyncPacket();
	public static final ActionPacket ACTION = new ActionPacket();
	public static final UndoPacket UNDO = new UndoPacket();

	public static void load() {
		Registries.PACKETS.register("turn", END_TURN);
		Registries.PACKETS.register("group_sync", GROUP_SYNC);
		Registries.PACKETS.register("action", ACTION);
		Registries.PACKETS.register("undo", UNDO);
	}

}
