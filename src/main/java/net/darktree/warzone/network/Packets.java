package net.darktree.warzone.network;

import net.darktree.warzone.Registries;
import net.darktree.warzone.network.packet.*;

public class Packets {

	public static final Packet.Type END_TURN = new Packet.Type(EndTurnPacket::new);
	public static final Packet.Type GROUP_SYNC = new Packet.Type(GroupSyncPacket::new);
	public static final Packet.Type ACTION = new Packet.Type(ActionPacket::new);
	public static final Packet.Type UNDO = new Packet.Type(UndoPacket::new);
	public static final Packet.Type COLONIZE = new Packet.Type(ColonizePacket::new);
	public static final Packet.Type PRODUCE = new Packet.Type(ProductionSyncPacket::new);

	public static void load() {
		Registries.PACKETS.register("turn", END_TURN);
		Registries.PACKETS.register("group_sync", GROUP_SYNC);
		Registries.PACKETS.register("action", ACTION);
		Registries.PACKETS.register("undo", UNDO);
		Registries.PACKETS.register("colonize", COLONIZE);
		Registries.PACKETS.register("produce", PRODUCE);
	}

}
