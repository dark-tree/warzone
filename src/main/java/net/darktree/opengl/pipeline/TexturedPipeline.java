package net.darktree.opengl.pipeline;

import net.darktree.opengl.image.Atlas;
import net.darktree.opengl.image.Texture;
import net.darktree.opengl.shader.Program;
import net.darktree.opengl.vertex.VertexBuffer;

import java.util.function.Consumer;

public class TexturedPipeline extends Pipeline {

	public Texture texture;

	public TexturedPipeline(VertexBuffer buffer, Program program, Texture texture, Consumer<Pipeline> prepare) {
		super(buffer, program, prepare);
		this.texture = texture;
	}

	public TexturedPipeline(VertexBuffer buffer, Program program, Atlas atlas, Consumer<Pipeline> prepare) {
		this(buffer, program, atlas.texture, prepare);
	}

	@Override
	protected void bind() {
		super.bind();
		this.texture.bind();
	}

	@Override
	public void close() {
		super.close();
		this.texture.close();
	}

}
