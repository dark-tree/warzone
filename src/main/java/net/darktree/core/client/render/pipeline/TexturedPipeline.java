package net.darktree.core.client.render.pipeline;

import net.darktree.core.client.render.image.Atlas;
import net.darktree.core.client.render.image.Font;
import net.darktree.core.client.render.image.Texture;
import net.darktree.core.client.render.shader.Program;
import net.darktree.core.client.render.vertex.VertexBuffer;

import java.util.function.Consumer;

public class TexturedPipeline extends Pipeline {

	private Texture texture;

	public TexturedPipeline(VertexBuffer buffer, Program program, Texture texture, Consumer<Pipeline> prepare) {
		super(buffer, program, prepare);
		this.texture = texture;
	}

	public TexturedPipeline(VertexBuffer buffer, Program program, Atlas atlas, Consumer<Pipeline> prepare) {
		this(buffer, program, atlas.getTexture(), prepare);
	}

	public TexturedPipeline(VertexBuffer buffer, Program program, Font font, Consumer<Pipeline> prepare) {
		this(buffer, program, font.getAtlas(), prepare);
	}

	public void setTexture(Texture texture) {
		this.texture = texture;
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
