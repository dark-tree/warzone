package net.darktree.warzone.network.packet;

import net.darktree.warzone.network.PacketBuffer;
import net.darktree.warzone.network.PacketContext;
import net.darktree.warzone.network.Packets;
import net.darktree.warzone.network.SimplePacket;
import net.darktree.warzone.util.math.MathHelper;
import net.darktree.warzone.world.action.ColonizeAction;

public class ColonizePacket  extends SimplePacket {

	private final int x, y;
	private final boolean war;

	public ColonizePacket(PacketBuffer buffer, PacketContext context) {
		super(Packets.COLONIZE);
		this.x = buffer.getInt();
		this.y = buffer.getInt();
		this.war = buffer.getBool();
	}

	public ColonizePacket(int x, int y, boolean war) {
		super(Packets.COLONIZE);
		this.x = x;
		this.y = y;
		this.war = war;
	}

	@Override
	protected PacketBuffer getBuffer() {
		PacketBuffer buffer = super.getBuffer();
		buffer.putInt(x);
		buffer.putInt(y);
		buffer.putBool(war);

		return buffer;
	}

	@Override
	public void apply(PacketContext context) {
		context.getWorld().getLedger().push(new ColonizeAction(MathHelper.nextRandomDice(false), x, y, war));
	}

}
