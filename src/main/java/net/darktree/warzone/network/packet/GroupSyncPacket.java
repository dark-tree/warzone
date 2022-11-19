package net.darktree.warzone.network.packet;

import net.darktree.warzone.network.*;

import java.nio.ByteBuffer;

public class GroupSyncPacket extends Packet<UserGroup> {

	@Override
	public UserGroup getListenerValue(Relay relay, ByteBuffer buffer) {
		relay.assertSide(Side.CLIENT);

		int host = buffer.getInt();
		int gid = buffer.getInt();
		int count = buffer.getInt();

		UserGroup group = new UserGroup(relay, host, gid);

		for (int i = 0; i < count; i ++) {
			group.join(buffer.getInt());
		}

		return group;
	}

	public PacketDelegate of(UserGroup group) {
		ByteBuffer buffer = getBuffer();

		buffer.putInt(group.host);
		buffer.putInt(group.id);
		buffer.putInt(group.users.size());
		group.users.forEach(buffer::putInt);

		return new PacketDelegate(buffer);
	}

}
