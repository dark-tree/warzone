package net.darktree.warzone.network;

import java.nio.ByteBuffer;

public abstract class VoidPacket extends Packet<Void> {

	public abstract void onReceive(Relay relay, ByteBuffer buffer);

	public Void getListenerValue(Relay relay, ByteBuffer buffer) {
		onReceive(relay, buffer);
		return null;
	}

}
