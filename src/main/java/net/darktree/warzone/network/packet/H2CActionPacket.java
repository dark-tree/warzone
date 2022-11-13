package net.darktree.warzone.network.packet;

import net.darktree.warzone.Main;
import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.network.Relay;
import net.darktree.warzone.network.UserGroup;
import net.darktree.warzone.network.VoidPacket;
import net.darktree.warzone.util.NBTHelper;
import net.darktree.warzone.world.WorldHolder;
import net.darktree.warzone.world.action.manager.Action;
import net.querz.nbt.tag.CompoundTag;

import java.nio.ByteBuffer;

public class H2CActionPacket extends VoidPacket {

	@Override
	public void onVoidReceive(Relay relay, ByteBuffer buffer) {
		Symbol symbol = Symbol.fromIndex(buffer.get());
		CompoundTag nbt = NBTHelper.readCompound(buffer);

		Main.runSynced(() -> {
			WorldHolder.world.getManager().applyReceived(symbol, nbt);
		});
	}

	private ByteBuffer getReadyBuffer(Symbol symbol, Action action) {
		ByteBuffer buffer = getBuffer();

		buffer.put((byte) symbol.ordinal());
		CompoundTag nbt = new CompoundTag();
		action.toNbt(nbt);
		NBTHelper.writeCompound(nbt, buffer);

		return buffer;
	}

	public void broadcast(Relay relay, Symbol symbol, Action action) {
		relay.broadcastMessage(getReadyBuffer(symbol, action));
	}

	public void send(Relay relay, UserGroup group, Symbol symbol, Action action) {
		relay.sendMessage(group.host, getReadyBuffer(symbol, action));
	}

}
