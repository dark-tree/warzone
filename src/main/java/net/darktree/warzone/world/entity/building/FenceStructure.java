package net.darktree.warzone.world.entity.building;

import net.darktree.warzone.util.Direction;
import net.darktree.warzone.world.WorldSnapshot;
import net.darktree.warzone.world.entity.Entities;

public class FenceStructure extends FacedStructure {

	public FenceStructure(WorldSnapshot world, int x, int y) {
		super(world, x, y, Entities.FENCE);
	}

	@Override
	public boolean canPenetrate(Direction vector) {
		return vector.getAxis() != facing.getAxis();
	}

}
