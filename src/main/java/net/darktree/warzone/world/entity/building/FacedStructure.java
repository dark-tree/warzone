package net.darktree.warzone.world.entity.building;

import net.darktree.warzone.util.Direction;
import net.darktree.warzone.world.WorldSnapshot;
import net.darktree.warzone.world.entity.Entity;
import net.querz.nbt.tag.CompoundTag;
import org.jetbrains.annotations.NotNull;

public abstract class FacedStructure extends Building {

	protected Direction facing;

	protected FacedStructure(WorldSnapshot world, int x, int y, Type type) {
		super(world, x, y, type);
	}

	public Entity copyFrom(Entity entity) {
		FacedStructure moving = (FacedStructure) entity;

		this.facing = moving.facing;
		return super.copyFrom(entity);
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
