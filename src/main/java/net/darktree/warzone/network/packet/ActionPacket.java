package net.darktree.warzone.network.packet;

import net.darktree.warzone.Main;
import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.network.Packets;
import net.darktree.warzone.network.Side;
import net.darktree.warzone.network.VoidPacket;
import net.darktree.warzone.util.NBTHelper;
import net.darktree.warzone.world.WorldHolder;
import net.darktree.warzone.world.action.manager.Action;
import net.querz.nbt.tag.CompoundTag;

import java.nio.ByteBuffer;

public class ActionPacket extends VoidPacket {

	private final Action action;
	private final Symbol symbol;

	public ActionPacket(Side side, ByteBuffer buffer) {
		super(Packets.ACTION);

		this.symbol = Symbol.fromIndex(buffer.get());
		this.action = Action.fromNbt(NBTHelper.readCompound(buffer), WorldHolder.world);

		Main.runSynced(() -> {
			WorldHolder.world.getManager().apply(symbol, action, true);
		});
	}

	public ActionPacket(Symbol symbol, Action action) {
		super(Packets.ACTION);

		this.action = action;
		this.symbol = symbol;
	}

	@Override
	public ByteBuffer getBuffer() {
		ByteBuffer buffer = super.getBuffer();

		buffer.put((byte) symbol.ordinal());
		CompoundTag nbt = new CompoundTag();
		action.toNbt(nbt);
		NBTHelper.writeCompound(nbt, buffer);

		return buffer;
	}

}
