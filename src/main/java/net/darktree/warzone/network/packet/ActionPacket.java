package net.darktree.warzone.network.packet;

import net.darktree.warzone.Main;
import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.network.PacketDelegate;
import net.darktree.warzone.network.Side;
import net.darktree.warzone.network.VoidPacket;
import net.darktree.warzone.util.NBTHelper;
import net.darktree.warzone.world.WorldHolder;
import net.darktree.warzone.world.action.manager.Action;
import net.querz.nbt.tag.CompoundTag;

import java.nio.ByteBuffer;

public class ActionPacket extends VoidPacket {

	@Override
	public void onReceive(Side side, ByteBuffer buffer) {
		Symbol symbol = Symbol.fromIndex(buffer.get());
		CompoundTag nbt = NBTHelper.readCompound(buffer);

		Main.runSynced(() -> {
			WorldHolder.world.getManager().apply(symbol, Action.fromNbt(nbt, WorldHolder.world), true);
		});
	}

	public PacketDelegate of(Symbol symbol, Action action) {
		ByteBuffer buffer = getBuffer();

		buffer.put((byte) symbol.ordinal());
		CompoundTag nbt = new CompoundTag();
		action.toNbt(nbt);
		NBTHelper.writeCompound(nbt, buffer);

		return new PacketDelegate(buffer);
	}

}
