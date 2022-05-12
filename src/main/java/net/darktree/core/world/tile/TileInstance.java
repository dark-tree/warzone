package net.darktree.core.world.tile;

import net.darktree.core.util.NbtSerializable;
import net.darktree.core.world.World;
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
