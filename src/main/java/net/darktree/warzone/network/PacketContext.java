package net.darktree.warzone.network;

import net.darktree.warzone.Game;
import net.darktree.warzone.Main;
import net.darktree.warzone.world.World;

import javax.annotation.Nullable;

public class PacketContext {

	public static PacketContext LOCAL = new PacketContext(null, null);

	private final Side side;
	private final Relay relay;

	public PacketContext(Side side, Relay relay) {
		this.side = side;
		this.relay = relay;
	}

	public void expect(Side expected) {
		if (side != expected) throw new AssertionError("Invalid side, expected: '" + expected + "', got: '" + side + "'!");
	}

	@Nullable
	public Relay getRelay() {
		return relay;
	}

	@Nullable
	public Side getSide() {
		return side;
	}

	public Game getGame() {
		return Main.game;
	}

	public World getWorld() {
		return getGame().getWorld().orElseThrow();
	}

	public boolean isLocal() {
		return relay == null;
	}

	public boolean isHost() {
		return side == Side.HOST;
	}

}
