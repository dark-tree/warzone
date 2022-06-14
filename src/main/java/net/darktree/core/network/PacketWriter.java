package net.darktree.core.network;

import java.io.OutputStream;

public class PacketWriter {

	private final OutputStream stream;

	public PacketWriter(OutputStream stream) {
		this.stream = stream;
	}

	public PacketSender of(PacketType type) {
		return new PacketSender(this.stream, type);
	}

}
