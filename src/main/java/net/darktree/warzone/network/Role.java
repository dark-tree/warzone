package net.darktree.warzone.network;

import net.darktree.warzone.network.urp.PacketType;
import net.darktree.warzone.network.urp.PacketWriter;

/**
 * This class describes the role of a Relay class, and what it will
 * do after establishing a connection with the URP server
 */
public abstract class Role {

	public final Side side;

	private Role(Side side) {
		this.side = side;
	}

	abstract void onConnect(PacketWriter writer);

	public static class Client extends Role {

		private final int gid;

		/**
		 * Creates a role that instructs the relay to join a group of the given id
		 */
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

		/**
		 * Creates a role that instructs the relay to create a new group
		 */
		public Host() {
			super(Side.HOST);
		}

		@Override
		void onConnect(PacketWriter writer) {
			writer.of(PacketType.U2R_MAKE).send();
		}

	}

}
