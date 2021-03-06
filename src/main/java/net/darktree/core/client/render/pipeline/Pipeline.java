package net.darktree.core.client.render.pipeline;

import net.darktree.core.client.render.shader.Program;
import net.darktree.core.client.render.vertex.VertexBuffer;
import org.lwjgl.opengl.GL32;

import java.util.function.Consumer;

public class Pipeline implements AutoCloseable {

	private final Consumer<Pipeline> prepare;
	private final boolean immediate;

	public final VertexBuffer buffer;
	public final Program program;

	public Pipeline(VertexBuffer buffer, Program program, Consumer<Pipeline> prepare, boolean immediate) {
		this.prepare = prepare;
		this.buffer = buffer;
		this.program = program;
		this.immediate = immediate;
	}

	protected void bind() {
		this.program.bind();
		this.buffer.bind();
	}

	public void flush() {
		if (!this.buffer.isEmpty()) {
			bind();

			this.prepare.accept(this);
			GL32.glDrawArrays(this.buffer.primitive, 0, this.buffer.count());
			if(immediate) this.buffer.clear();
		}
	}

	@Override
	public void close() {
		this.buffer.close();
	}

}
