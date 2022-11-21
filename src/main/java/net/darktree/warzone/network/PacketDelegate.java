package net.darktree.warzone.network;

import net.darktree.warzone.Registries;

import java.nio.ByteBuffer;

public class PacketDelegate {

	private final ByteBuffer buffer;

	public PacketDelegate(ByteBuffer buffer) {
		this.buffer = buffer;
	}

	public void broadcast() {
		UserGroup group = UserGroup.instance;

		if (group == null) {
			handleLocal();
			return;
		}

		group.relay.broadcastMessage(this.buffer);
	}

	public void sendToUser(int uid) {
		UserGroup group = UserGroup.instance;

		if (group == null) {
			handleLocal();
			return;
		}

		if (uid == -1) {
			uid = group.host;
		}

		group.relay.sendMessage(uid, this.buffer);
	}

	public void sendToHost() {
		sendToUser(-1);
	}

	private void handleLocal() {
		Registries.PACKETS.getElement(buffer.getInt()).getListenerValue(Side.HOST, buffer);
	}

}
