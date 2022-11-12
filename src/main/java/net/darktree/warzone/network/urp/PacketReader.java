package net.darktree.warzone.network.urp;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Objects;

public class PacketReader {

	private final InputStream stream;
	private final Map<PacketType, PacketCallback> types = new IdentityHashMap<>();

	public PacketReader(InputStream stream) {
		this.stream = stream;
	}

	public void next() throws IOException {
		PacketType type = PacketType.decode(stream.read());
		short size = (short) (stream.read() | (stream.read() >> 8));

		apply(type, ByteBuffer.wrap(stream.readNBytes(size)));
	}

	public void on(PacketType type, PacketCallback handler) {
		types.put(type, handler);
	}

	private void apply(PacketType type, ByteBuffer body) {
		body.order(ByteOrder.nativeOrder());

		try {
			Objects.requireNonNull(types.get(type)).call(body);
		} catch (NullPointerException e) {
			e.printStackTrace();
			throw new RuntimeException("No callback for packet type '" + type + "' defined!");
		}
	}

}
