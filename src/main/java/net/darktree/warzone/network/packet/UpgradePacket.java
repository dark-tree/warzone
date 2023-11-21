package net.darktree.warzone.network.packet;

import net.darktree.warzone.Registries;
import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.country.upgrade.Upgrade;
import net.darktree.warzone.network.PacketBuffer;
import net.darktree.warzone.network.PacketContext;
import net.darktree.warzone.network.Packets;
import net.darktree.warzone.network.SimplePacket;

public class UpgradePacket extends SimplePacket {

	private final Symbol symbol;
	private final Upgrade<?> upgrade;

	public UpgradePacket(PacketBuffer buffer, PacketContext context) {
		super(Packets.UPGRADE);
		this.symbol = buffer.getEnum(Symbol.class);
		this.upgrade = buffer.getElement(Registries.UPGRADES);
	}

	public UpgradePacket(Symbol symbol, Upgrade<?> upgrade) {
		super(Packets.UPGRADE);
		this.symbol = symbol;
		this.upgrade = upgrade;
	}

	@Override
	public void apply(PacketContext context) {
		if (upgrade.buy(context.getWorld().getTrackingWorld().getCountry(symbol)) && context.isHost()) {
			broadcastExceptHost();
		}
	}

	@Override
	public PacketBuffer getBuffer() {
		PacketBuffer buffer = super.getBuffer();

		buffer.putEnum(symbol);
		buffer.putElement(upgrade);

		return buffer;
	}

}
