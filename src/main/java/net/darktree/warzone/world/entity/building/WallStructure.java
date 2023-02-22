package net.darktree.warzone.world.entity.building;

import net.darktree.warzone.util.Direction;
import net.darktree.warzone.world.World;
import net.darktree.warzone.world.entity.Entities;

public class WallStructure extends Building {

	protected Direction facing = Direction.NORTH;

	public WallStructure(World world, int x, int y) {
		this(world, x, y, Entities.WALL);
	}

	protected WallStructure(World world, int x, int y, Building.Type type) {
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
	public boolean canPenetrate(Direction vector) {
		return vector != facing.opposite();
	}

}
