package net.darktree.warzone.network.packet;

import net.darktree.warzone.network.Packets;
import net.darktree.warzone.network.Side;
import net.darktree.warzone.network.SimplePacket;
import net.darktree.warzone.util.math.MathHelper;
import net.darktree.warzone.world.WorldHolder;
import net.darktree.warzone.world.action.ColonizeAction;

import java.nio.ByteBuffer;

public class ColonizePacket  extends SimplePacket {

	private final int x, y;
	private final boolean war;

	public ColonizePacket(Side side, ByteBuffer buffer) {
		super(Packets.COLONIZE);
		this.x = buffer.getInt();
		this.y = buffer.getInt();
		this.war = buffer.get() != 0;
	}

	public ColonizePacket(int x, int y, boolean war) {
		super(Packets.COLONIZE);
		this.x = x;
		this.y = y;
		this.war = war;
	}

	@Override
	protected ByteBuffer getBuffer() {
		ByteBuffer buffer = super.getBuffer();
		buffer.putInt(x);
		buffer.putInt(y);
		buffer.put((byte) (war ? 1 : 0));

		return buffer;
	}

	@Override
	public void apply() {
		WorldHolder.world.getManager().apply(new ColonizeAction(WorldHolder.world, MathHelper.nextRandomDice(), x, y, war));
	}

}