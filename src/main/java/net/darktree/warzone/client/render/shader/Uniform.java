package net.darktree.warzone.client.render.shader;

import net.darktree.warzone.client.render.ResizeableBuffer;
import org.lwjgl.opengl.GL32;

import java.nio.ByteBuffer;

public class Uniform {

	public final int location;
	private final Sender sender;
	private final ResizeableBuffer buffer;

	public Uniform(int location, Sender sender) {
		this.location = location;
		this.sender = sender;
		this.buffer = new ResizeableBuffer(1);
	}

	public void flush() {
		this.sender.send(this.location, this.buffer.asByteBuffer().flip());
		this.buffer.clear();
	}

	public Uniform putByte(byte value) {
		this.buffer.reserve(1).putByte(value);
		return this;
	}

	public Uniform putInt(int value) {
		this.buffer.reserve(4).putInt(value);
		return this;
	}

	public Uniform putFloat(float value) {
		this.buffer.reserve(4).putFloat(value);
		return this;
	}

	public Uniform putFloats(float... values) {
		this.buffer.reserve(values.length * 4).putFloats(values);
		return this;
	}

	@FunctionalInterface
	public interface Sender {
		void send(int loc, ByteBuffer buf);
	}

	public static final Sender INT = (loc, buf) -> GL32.glUniform1i(loc, buf.getInt());
	public static final Sender VEC1F = (loc, buf) -> GL32.glUniform1f(loc, buf.getFloat());
	public static final Sender VEC2F = (loc, buf) -> GL32.glUniform2f(loc, buf.getFloat(), buf.getFloat());
	public static final Sender VEC3F = (loc, buf) -> GL32.glUniform3f(loc, buf.getFloat(), buf.getFloat(), buf.getFloat());

}
