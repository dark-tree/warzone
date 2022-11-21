package net.darktree.warzone.network.packet;

import net.darktree.warzone.Main;
import net.darktree.warzone.network.PacketDelegate;
import net.darktree.warzone.network.Side;
import net.darktree.warzone.network.VoidPacket;
import net.darktree.warzone.world.WorldHolder;

import java.nio.ByteBuffer;

public class EndTurnPacket extends VoidPacket {

	@Override
	public void onReceive(Side side, ByteBuffer buffer) {
		Main.runSynced(() -> {
			WorldHolder.world.nextPlayerTurn();
		});
	}

	public PacketDelegate of() {
		return new PacketDelegate(getBuffer());
	}

}
