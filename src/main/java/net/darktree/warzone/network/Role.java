package net.darktree.warzone.network;

import net.darktree.warzone.network.urp.PacketType;
import net.darktree.warzone.network.urp.PacketWriter;

public abstract class Role {

	public final Side side;

	private Role(Side side) {
		this.side = side;
	}

	abstract void onConnect(PacketWriter writer);

	public static class Client extends Role {

		private final int gid;

		public Client(int gid) {
			super(Side.CLIENT);
			this.gid = gid;
		}

		@Override
		void onConnect(PacketWriter writer) {
			writer.of(PacketType.U2R_JOIN).write(gid).send();
		}

	}

	public static class Host extends Role {

		public Host() {
			super(Side.HOST);
		}

		@Override
		void onConnect(PacketWriter writer) {
			writer.of(PacketType.U2R_MAKE).send();
		}

	}

}
