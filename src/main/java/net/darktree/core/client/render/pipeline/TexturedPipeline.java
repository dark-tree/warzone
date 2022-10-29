package net.darktree.core.client.render.pipeline;

import net.darktree.core.client.render.image.Texture;
import net.darktree.core.client.render.image.TextureConvertible;
import net.darktree.core.client.render.shader.Program;
import net.darktree.core.client.render.vertex.VertexBuffer;

import java.util.function.Consumer;

public class TexturedPipeline extends Pipeline {

	private final Texture texture;

	public TexturedPipeline(VertexBuffer buffer, Program program, TextureConvertible texture, Consumer<Pipeline> prepare) {
		super(buffer, program, prepare);
		this.texture = texture.getTexture();
	}

	@Override
	protected void bind() {
		super.bind();
		this.texture.bind();
	}

	@Override
	public void close() {
		super.close();
	}

}
