package net.darktree.warzone.network.urp;

import java.nio.ByteBuffer;

@FunctionalInterface
public interface PacketCallback {

	void call(ByteBuffer buffer);

}
