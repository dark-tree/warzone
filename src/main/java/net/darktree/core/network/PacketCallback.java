package net.darktree.core.network;

import java.nio.ByteBuffer;

@FunctionalInterface
public interface PacketCallback {

	void call(ByteBuffer buffer);

}
