package net.darktree.core.client.render.pipeline;

import net.darktree.core.client.render.image.TextureConvertible;
import net.darktree.core.client.render.shader.Program;
import net.darktree.core.client.render.shader.Uniform;
import net.darktree.core.client.render.vertex.VertexBuffer;
import net.darktree.core.world.WorldView;

public class WorldPipeline extends Pipeline {

	private WorldPipeline(VertexBuffer.Builder builder, Program shader, TextureConvertible texture, WorldView view, Uniform scale, Uniform offset) {
		super(builder.build(), shader, texture, pipeline -> {
			scale.putFloats(view.scaleX, view.scaleY).flush();
			offset.putFloats(view.offsetX, view.offsetY).flush();
		});
	}

	public static WorldPipeline of(VertexBuffer.Builder builder, Program shader, TextureConvertible texture, WorldView view) {
		final Uniform scale = shader.uniform("scale", Uniform.VEC2F);
		final Uniform offset = shader.uniform("offset", Uniform.VEC2F);

		return new WorldPipeline(builder, shader, texture, view, scale, offset);
	}

}
