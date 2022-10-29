package net.darktree.core.client.render.pipeline;

import net.darktree.core.client.render.image.Texture;
import net.darktree.core.client.render.image.TextureConvertible;
import net.darktree.core.client.render.shader.Program;
import net.darktree.core.client.render.vertex.VertexBuffer;

import java.util.function.Consumer;

public class Pipeline implements AutoCloseable {

	private final Consumer<Pipeline> prepare;

	public final VertexBuffer buffer;
	protected final Program program;
	protected final Texture texture;

	public Pipeline(VertexBuffer buffer, Program program, TextureConvertible texture, Consumer<Pipeline> prepare) {
		this.prepare = prepare;
		this.buffer = buffer;
		this.program = program;
		this.texture = texture.getTexture();
	}

	public Pipeline(VertexBuffer buffer, Program program, TextureConvertible texture) {
		this(buffer, program, texture, pipeline -> {});
	}

	protected void bind() {
		this.program.bind();
		this.buffer.bind();
		this.texture.bind();
	}

	public void flush() {
		if (!this.buffer.isEmpty()) {
			bind();

			this.prepare.accept(this);
			this.buffer.draw();
		}
	}

	@Override
	public void close() {
		this.buffer.close();
	}

}
