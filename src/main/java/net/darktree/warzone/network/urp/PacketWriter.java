package net.darktree.warzone.network.urp;

import net.darktree.warzone.util.Logger;

import java.io.OutputStream;

public class PacketWriter {

	private final OutputStream stream;

	public PacketWriter(OutputStream stream) {
		this.stream = stream;
	}

	public PacketSender of(PacketType type) {
		return new PacketSender(this.stream, type);
	}

	public void flush() {
		try {
			stream.flush();
		} catch (Exception e) {
			Logger.warn("Failed to flush socket output stream!");
		}
	}

}
