package net.darktree.core.client.render;

import net.darktree.core.client.Buffers;
import net.darktree.core.client.Shaders;
import net.darktree.core.client.Sprites;
import net.darktree.core.client.render.pipeline.Pipeline;
import net.darktree.core.client.render.pipeline.WorldPipeline;
import net.darktree.core.client.render.vertex.VertexBuffer;
import net.darktree.core.world.World;
import net.darktree.core.world.WorldView;

public class WorldBuffers implements AutoCloseable {

	private final Pipeline surface;
	private final Pipeline buildings;
	private final Pipeline entities;
	private final Pipeline overlay;

	public WorldBuffers(World world) {
		final WorldView view = world.getView();

		surface = WorldPipeline.of(Buffers.STATIC, Shaders.WORLD, Sprites.ATLAS, view);
		buildings = WorldPipeline.of(Buffers.STATIC, Shaders.WORLD, Sprites.ATLAS, view);
		entities = WorldPipeline.of(Buffers.IMMEDIATE, Shaders.WORLD, Sprites.ATLAS, view);
		overlay = WorldPipeline.of(Buffers.IMMEDIATE, Shaders.COLOR, Sprites.UNSET, view);
	}

	public void draw() {
		surface.flush();
		buildings.flush();
		entities.flush();
		overlay.flush();
	}

	@Override
	public void close() {
		surface.close();
		buildings.close();
		entities.close();
		overlay.close();
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

	public VertexBuffer getOverlay() {
		return overlay.buffer;
	}

}
