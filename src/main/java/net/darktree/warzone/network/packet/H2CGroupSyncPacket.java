package net.darktree.warzone.network.packet;

import net.darktree.warzone.network.Packet;
import net.darktree.warzone.network.Relay;
import net.darktree.warzone.network.UserGroup;

import java.nio.ByteBuffer;

public class H2CGroupSyncPacket extends Packet<UserGroup> {

	@Override
	public UserGroup onReceive(Relay relay, ByteBuffer buffer) {
		int host = buffer.getInt();
		int gid = buffer.getInt();
		int count = buffer.getInt();

		UserGroup group = new UserGroup(relay, host, gid);

		for (int i = 0; i < count; i ++) {
			group.join(buffer.getInt());
		}

		return group;
	}

	public void send(Relay relay, UserGroup group) {
		ByteBuffer buffer = getBuffer();

		buffer.putInt(group.host);
		buffer.putInt(group.group);
		buffer.putInt(group.users.size());
		group.users.forEach(buffer::putInt);

		relay.broadcastMessage(buffer);
	}

}
