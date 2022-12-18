package net.darktree.warzone.network;

public abstract class SimplePacket extends Packet<Void> {

	public SimplePacket(Type type) {
		super(type);
	}

	@Override
	public Void getListenerValue() {
		return null;
	}

}
