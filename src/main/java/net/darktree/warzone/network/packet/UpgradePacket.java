package net.darktree.warzone.network.packet;

import net.darktree.warzone.Registries;
import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.country.upgrade.Upgrade;
import net.darktree.warzone.network.Packets;
import net.darktree.warzone.network.Relay;
import net.darktree.warzone.network.Side;
import net.darktree.warzone.network.SimplePacket;
import net.darktree.warzone.world.WorldHolder;

import java.nio.ByteBuffer;

public class UpgradePacket extends SimplePacket {

	private final Symbol symbol;
	private final Upgrade<?> upgrade;
	private final Side side;

	public UpgradePacket(ByteBuffer buffer, Side side, Relay relay) {
		super(Packets.UPGRADE);
		this.symbol = Symbol.fromIndex(buffer.get());
		this.upgrade = Registries.UPGRADES.byId(buffer.getInt()).value();
		this.side = side;
	}

	public UpgradePacket(Symbol symbol, Upgrade<?> upgrade) {
		super(Packets.UPGRADE);
		this.symbol = symbol;
		this.upgrade = upgrade;
		this.side = null;
	}

	@Override
	public void apply() {
		if (upgrade.buy(WorldHolder.world.getCountry(symbol)) && side == Side.HOST) {
			broadcastExceptHost();
		}
	}

	@Override
	public ByteBuffer getBuffer() {
		ByteBuffer buffer = super.getBuffer();
		buffer.put((byte) symbol.ordinal());
		buffer.putInt(upgrade.id());
		return buffer;
	}

}
