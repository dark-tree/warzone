package net.darktree.warzone.network.urp;

public enum PacketType {

	U2R_MAKE(0x00), // create new user group
	U2R_JOIN(0x01), // join a user group
	U2R_QUIT(0x02), // exit a user group
	U2R_BROD(0x03), // broadcast a message in a user group
	U2R_SEND(0x04), // send a message to a specific user of a user group

	R2U_WELC(0x10), // send to newly joined users
	R2U_TEXT(0x11), // message received
	R2U_MADE(0x12), // new user group made
	R2U_JOIN(0x13), // user joined group
	R2U_LEFT(0x14), // user left group (to host)
	R2U_EXIT(0x15); // user left group (to user)

	public final byte value;

	PacketType(int value) {
		this.value = (byte) value;
	}

	public static PacketType decode(int type) {
		return values()[type - 0x10 + R2U_WELC.ordinal()];
	}

}
