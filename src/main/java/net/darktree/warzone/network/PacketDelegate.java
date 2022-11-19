package net.darktree.warzone.network;

import java.nio.ByteBuffer;

public class PacketDelegate {

	private final ByteBuffer buffer;

	public PacketDelegate(ByteBuffer buffer) {
		this.buffer = buffer;
	}

	public void broadcast(UserGroup group) {
		group.relay.broadcastMessage(this.buffer);
	}

	public void sendToHost(UserGroup group) {
		group.relay.sendMessage(group.host, this.buffer);
	}

}
