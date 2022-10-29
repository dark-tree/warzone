package net.darktree.core.client.render.pipeline;

import net.darktree.core.client.render.shader.Program;
import net.darktree.core.client.render.vertex.VertexBuffer;

import java.util.function.Consumer;

public class Pipeline implements AutoCloseable {

	private final Consumer<Pipeline> prepare;

	public final VertexBuffer buffer;
	public final Program program;

	public Pipeline(VertexBuffer buffer, Program program, Consumer<Pipeline> prepare) {
		this.prepare = prepare;
		this.buffer = buffer;
		this.program = program;
	}

	protected void bind() {
		this.program.bind();
		this.buffer.bind();
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
