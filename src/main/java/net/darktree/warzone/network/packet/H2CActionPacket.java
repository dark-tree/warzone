package net.darktree.warzone.network.packet;

import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.network.Packet;
import net.darktree.warzone.network.Relay;
import net.darktree.warzone.util.NBTHelper;
import net.darktree.warzone.world.WorldHolder;
import net.darktree.warzone.world.action.manager.Action;
import net.querz.nbt.tag.CompoundTag;

import java.nio.ByteBuffer;

public class H2CActionPacket extends Packet {

	@Override
	public void onReceive(ByteBuffer buffer) {
		Symbol symbol = Symbol.fromIndex(buffer.get());
		CompoundTag nbt = NBTHelper.readCompound(buffer);
		WorldHolder.world.getManager().applyReceived(symbol, nbt);
	}

	public void send(Relay relay, Symbol symbol, Action action) {
		ByteBuffer buffer = getBuffer();

		buffer.put((byte) symbol.ordinal());
		CompoundTag nbt = new CompoundTag();
		action.toNbt(nbt);
		NBTHelper.writeCompound(nbt, buffer);

		relay.broadcastMessage(buffer);
	}

}
