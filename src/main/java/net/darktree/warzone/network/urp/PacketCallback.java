package net.darktree.warzone.network.urp;

import net.darktree.warzone.network.PacketBuffer;

@FunctionalInterface
public interface PacketCallback {

	void call(PacketBuffer buffer);

}
