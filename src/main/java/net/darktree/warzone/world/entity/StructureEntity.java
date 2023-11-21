package net.darktree.warzone.world.entity;

import net.darktree.warzone.client.render.WorldBuffers;
import net.darktree.warzone.client.render.vertex.VertexBuffer;
import net.darktree.warzone.world.WorldSnapshot;

public abstract class StructureEntity extends Entity {

	protected final int width, height;

	public StructureEntity(WorldSnapshot world, int x, int y, int w, int h, Entity.Type type) {
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

	abstract protected void draw(VertexBuffer buffer);

}
