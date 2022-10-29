package net.darktree.core.world.entity;

import net.darktree.core.client.render.WorldBuffers;
import net.darktree.core.client.render.vertex.VertexBuffer;
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

	@Override
	public void draw(WorldBuffers buffers, boolean updateStaticElements) {
		if (updateStaticElements) draw(buffers.getBuilding());
	}

	abstract public void draw(VertexBuffer buffer);

}
