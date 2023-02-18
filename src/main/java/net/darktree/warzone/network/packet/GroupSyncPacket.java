package net.darktree.warzone.network.packet;

import net.darktree.warzone.network.*;

public class GroupSyncPacket extends Packet<UserGroup> {

	private final UserGroup group;

	public GroupSyncPacket(PacketBuffer buffer, PacketContext context) {
		super(Packets.GROUP_SYNC);
		context.expect(Side.CLIENT);

		int host = buffer.getInt();
		int gid = buffer.getInt();
		int count = buffer.getInt();

		this.group = new UserGroup(context.getRelay(), host, gid);

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
	public PacketBuffer getBuffer() {
		PacketBuffer buffer = super.getBuffer();

		buffer.putInt(group.host);
		buffer.putInt(group.id);
		buffer.putInt(group.users.size());
		group.users.forEach(buffer::putInt);

		return buffer;
	}

}
