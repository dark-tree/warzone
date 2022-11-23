package net.darktree.warzone.network;

public abstract class VoidPacket extends Packet<Void> {

	public VoidPacket(Type type) {
		super(type);
	}

	@Override
	public Void getListenerValue() {
		return null;
	}

}
