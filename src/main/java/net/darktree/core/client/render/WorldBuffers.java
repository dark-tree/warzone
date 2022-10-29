package net.darktree.core.client.render;

import net.darktree.core.client.Buffers;
import net.darktree.core.client.render.pipeline.Pipeline;
import net.darktree.core.client.render.pipeline.WorldPipeline;
import net.darktree.core.client.render.vertex.VertexBuffer;
import net.darktree.core.world.World;

public class WorldBuffers implements AutoCloseable {

	private final Pipeline surface;
	private final Pipeline buildings;
	private final Pipeline entities;

	public WorldBuffers(World world) {
		surface = new WorldPipeline(Buffers.STATIC, world.getView());
		buildings = new WorldPipeline(Buffers.IMMEDIATE, world.getView()); // TODO move to a static buffer
		entities = new WorldPipeline(Buffers.IMMEDIATE, world.getView());
	}

	public void draw() {
		surface.flush();
		buildings.flush();
		entities.flush();
	}

	@Override
	public void close() {
		surface.close();
		buildings.close();
		entities.close();
	}

	public VertexBuffer getSurface() {
		return surface.buffer;
	}

	public VertexBuffer getBuilding() {
		return buildings.buffer;
	}

	public VertexBuffer getEntity() {
		return entities.buffer;
	}

}
