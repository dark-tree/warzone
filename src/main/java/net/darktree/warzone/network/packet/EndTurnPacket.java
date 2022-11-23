package net.darktree.warzone.network.packet;

import net.darktree.warzone.Main;
import net.darktree.warzone.network.Packets;
import net.darktree.warzone.network.Side;
import net.darktree.warzone.network.VoidPacket;
import net.darktree.warzone.world.WorldHolder;

import java.nio.ByteBuffer;

public class EndTurnPacket extends VoidPacket {

	public EndTurnPacket(Side side, ByteBuffer buffer) {
		super(Packets.END_TURN);

		Main.runSynced(() -> {
			WorldHolder.world.nextPlayerTurn();
		});
	}

	public EndTurnPacket() {
		super(Packets.END_TURN);
	}

}
