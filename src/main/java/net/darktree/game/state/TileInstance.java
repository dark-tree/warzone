package net.darktree.game.state;

import net.darktree.game.World;
import net.darktree.game.nbt.NbtSerializable;
import net.querz.nbt.tag.CompoundTag;
import org.jetbrains.annotations.NotNull;

public class TileInstance implements NbtSerializable {

	protected final World world;
	protected final int x, y;

	public TileInstance(World world, int x, int y) {
		this.world = world;
		this.x = x;
		this.y = y;
	}

	@Override
	public void toNbt(@NotNull CompoundTag tag) {

	}

	@Override
	public void fromNbt(@NotNull CompoundTag tag) {

	}

}
