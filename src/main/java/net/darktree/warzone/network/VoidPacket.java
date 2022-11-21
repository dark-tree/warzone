package net.darktree.warzone.network;

import java.nio.ByteBuffer;

public abstract class VoidPacket extends Packet<Void> {

	public abstract void onReceive(Side side, ByteBuffer buffer);

	public Void getListenerValue(Side side, ByteBuffer buffer) {
		onReceive(side, buffer);
		return null;
	}

}
