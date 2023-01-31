package net.darktree.warzone.network.packet;

import net.darktree.warzone.Registries;
import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.country.upgrade.Upgrade;
import net.darktree.warzone.network.PacketContext;
import net.darktree.warzone.network.Packets;
import net.darktree.warzone.network.SimplePacket;

import java.nio.ByteBuffer;

public class UpgradePacket extends SimplePacket {

	private final Symbol symbol;
	private final Upgrade<?> upgrade;

	public UpgradePacket(ByteBuffer buffer, PacketContext context) {
		super(Packets.UPGRADE);
		this.symbol = Symbol.fromIndex(buffer.get());
		this.upgrade = Registries.UPGRADES.byId(buffer.getInt()).value();
	}

	public UpgradePacket(Symbol symbol, Upgrade<?> upgrade) {
		super(Packets.UPGRADE);
		this.symbol = symbol;
		this.upgrade = upgrade;
	}

	@Override
	public void apply(PacketContext context) {
		if (upgrade.buy(context.getWorld().getCountry(symbol)) && context.isHost()) {
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
