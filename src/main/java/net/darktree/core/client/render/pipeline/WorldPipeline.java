package net.darktree.core.client.render.pipeline;

import net.darktree.core.client.Shaders;
import net.darktree.core.client.Sprites;
import net.darktree.core.client.Uniforms;
import net.darktree.core.client.render.vertex.VertexBuffer;
import net.darktree.core.world.WorldView;

public class WorldPipeline extends TexturedPipeline {

	public WorldPipeline(VertexBuffer.Builder builder, WorldView view) {
		super(builder.build(), Shaders.WORLD, Sprites.ATLAS, pipeline -> {
			Uniforms.SCALE.putFloats(view.scaleX, view.scaleY).flush();
			Uniforms.OFFSET.putFloats(view.offsetX, view.offsetY).flush();
		});
	}

}
