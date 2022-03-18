package net.darktree.opengl.vertex;

import net.darktree.opengl.image.Sprite;
import org.lwjgl.opengl.GL32;


public class Renderer {

	public static void quad(VertexBuffer buffer, float x, float y, float w, float h, Sprite sprite) {
		vertex(buffer, x, y, sprite.u1(), sprite.v1());
		vertex(buffer, x + w, y, sprite.u2(), sprite.v1());
		vertex(buffer, x, y + h, sprite.u1(), sprite.v2());

		vertex(buffer, x + w, y, sprite.u2(), sprite.v1());
		vertex(buffer, x + w, y + h, sprite.u2(), sprite.v2());
		vertex(buffer, x, y + h, sprite.u1(), sprite.v2());
	}

	public static void vertex(VertexBuffer buffer, float x, float y, float u, float v) {
		buffer.putFloat(x).putFloat(y).putFloat(u).putFloat(v);
	}

	public static void clear() {
		GL32.glClear(GL32.GL_COLOR_BUFFER_BIT | GL32.GL_DEPTH_BUFFER_BIT);
	}

}
