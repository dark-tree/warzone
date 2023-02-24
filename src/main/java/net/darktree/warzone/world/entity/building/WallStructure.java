package net.darktree.warzone.world.entity.building;

import net.darktree.warzone.util.Direction;
import net.darktree.warzone.world.World;
import net.darktree.warzone.world.entity.Entities;

public class WallStructure extends FacedStructure {

	public WallStructure(World world, int x, int y) {
		super(world, x, y, Entities.WALL);
	}

	@Override
	public boolean canPenetrate(Direction vector) {
		return vector != facing.opposite();
	}

}
