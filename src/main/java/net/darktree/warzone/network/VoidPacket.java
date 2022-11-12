package net.darktree.warzone.network;

import java.nio.ByteBuffer;

public abstract class VoidPacket extends Packet<Void> {

	abstract public void onVoidReceive(Relay relay,ByteBuffer buffer);

	@Override
	public Void onReceive(Relay relay, ByteBuffer buffer) {
		onVoidReceive(relay, buffer);
		return null;
	}

}
