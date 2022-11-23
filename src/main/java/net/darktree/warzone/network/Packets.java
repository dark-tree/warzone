package net.darktree.warzone.network;

import net.darktree.warzone.Registries;
import net.darktree.warzone.network.packet.ActionPacket;
import net.darktree.warzone.network.packet.EndTurnPacket;
import net.darktree.warzone.network.packet.GroupSyncPacket;
import net.darktree.warzone.network.packet.UndoPacket;

public class Packets {

	public static final Packet.Type END_TURN = new Packet.Type(EndTurnPacket::new);
	public static final Packet.Type GROUP_SYNC = new Packet.Type(GroupSyncPacket::new);
	public static final Packet.Type ACTION = new Packet.Type(ActionPacket::new);
	public static final Packet.Type UNDO = new Packet.Type(UndoPacket::new);

	public static void load() {
		Registries.PACKETS.register("turn", END_TURN);
		Registries.PACKETS.register("group_sync", GROUP_SYNC);
		Registries.PACKETS.register("action", ACTION);
		Registries.PACKETS.register("undo", UNDO);
	}

}
