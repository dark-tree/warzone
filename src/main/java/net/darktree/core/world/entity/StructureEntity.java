package net.darktree.core.world.entity;

import net.darktree.core.world.World;

public abstract class StructureEntity extends Entity {

	protected final int width, height;

	public StructureEntity(World world, int x, int y, int w, int h, Entity.Type type) {
		super(world, x, y, type);

		width = w;
		height = h;
	}

	@Override
	public boolean isAt(int x, int y) {
		int a = x - this.tx;
		int b = y - this.ty;

		if (a < 0 || b < 0) {
			return false;
		}

		return a < width && b < height;
	}

}
