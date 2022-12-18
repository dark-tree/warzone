package net.darktree.warzone.network.packet;

import net.darktree.warzone.network.*;

import java.nio.ByteBuffer;

public class GroupSyncPacket extends Packet<UserGroup> {

	private final UserGroup group;

	public GroupSyncPacket(ByteBuffer buffer, Side side, Relay relay) {
		super(Packets.GROUP_SYNC);
		side.expect(Side.CLIENT);

		int host = buffer.getInt();
		int gid = buffer.getInt();
		int count = buffer.getInt();

		this.group = new UserGroup(relay, host, gid);

		for (int i = 0; i < count; i ++) {
			group.join(buffer.getInt());
		}
	}

	public GroupSyncPacket(UserGroup group) {
		super(Packets.GROUP_SYNC);
		this.group = group;
	}

	@Override
	public UserGroup getListenerValue() {
		return group;
	}

	@Override
	public ByteBuffer getBuffer() {
		ByteBuffer buffer = super.getBuffer();

		buffer.putInt(group.host);
		buffer.putInt(group.id);
		buffer.putInt(group.users.size());
		group.users.forEach(buffer::putInt);

		return buffer;
	}

}
