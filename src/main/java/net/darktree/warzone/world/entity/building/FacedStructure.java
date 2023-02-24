package net.darktree.warzone.world.entity.building;

import net.darktree.warzone.util.Direction;
import net.darktree.warzone.world.World;
import net.querz.nbt.tag.CompoundTag;
import org.jetbrains.annotations.NotNull;

public abstract class FacedStructure extends Building {

	protected Direction facing;

	protected FacedStructure(World world, int x, int y, Type type) {
		super(world, x, y, type);
	}

	public void setFacing(Direction facing) {
		this.facing = facing;
	}

	@Override
	public Direction getRotation() {
		return facing;
	}

	@Override
	public void fromNbt(@NotNull CompoundTag nbt) {
		super.fromNbt(nbt);
		this.facing = Direction.values()[nbt.getInt("facing")];
	}

	@Override
	public void toNbt(@NotNull CompoundTag nbt) {
		super.toNbt(nbt);
		nbt.putInt("facing", facing.ordinal());
	}

}
