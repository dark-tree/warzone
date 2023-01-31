package net.darktree.warzone.client.render.vertex;

import net.darktree.warzone.Main;
import net.darktree.warzone.client.Sprites;
import net.darktree.warzone.client.render.Alignment;
import net.darktree.warzone.client.render.color.Color;
import net.darktree.warzone.client.render.image.Font;
import net.darktree.warzone.client.render.image.Sprite;
import org.lwjgl.opengl.GL32;

import java.nio.charset.StandardCharsets;


public class Renderer {

	public static void overlay(VertexBuffer buffer, int x, int y, Color color) {
		quad(buffer, x, y, 1, 1, Sprites.NONE, color.r, color.g, color.b, color.a);
	}

	public static void tile(VertexBuffer buffer, int x, int y, Sprite sprite) {
		quad(buffer, x, y, 1, 1, sprite, 0, 0, 0, 0);
	}

	public static void quad(VertexBuffer buffer, float x, float y, float w, float h, Sprite sprite, float r, float g, float b, float a) {
		quad(buffer, x, y, x + w, y, x + w, y + h, x, y + h, sprite, r, g, b, a);
	}

	public static void quad(VertexBuffer buffer, float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4, Sprite sprite, float r, float g, float b, float a) {
		vertex(buffer, x1, y1, sprite.u1(), sprite.v1(), r, g, b, a);
		vertex(buffer, x2, y2, sprite.u2(), sprite.v1(), r, g, b, a);
		vertex(buffer, x4, y4, sprite.u1(), sprite.v2(), r, g, b, a);

		vertex(buffer, x2, y2, sprite.u2(), sprite.v1(), r, g, b, a);
		vertex(buffer, x3, y3, sprite.u2(), sprite.v2(), r, g, b, a);
		vertex(buffer, x4, y4, sprite.u1(), sprite.v2(), r, g, b, a);
	}

	public static void vertex(VertexBuffer buffer, float x, float y, float u, float v, float r, float g, float b, float a) {
		buffer.putFloat(x).putFloat(y).putFloat(u).putFloat(v).putFloat(r).putFloat(g).putFloat(b).putFloat(a);
	}

	public static void text(String text, Font font, VertexBuffer buffer, float x, float y, float width, float height, float r, float g, float b, float a, Alignment alignment) {
		float offset = alignment.getOffset(text.length() * width * font.spacing);

		for (byte chr : text.getBytes(StandardCharsets.UTF_8)) {
			if (chr == '\n') {
				y -= height;
				offset = 0;
				continue;
			}

			Sprite sprite = font.sprite(chr);
			Renderer.quad(buffer, x + offset, y, width, height, sprite, r, g, b, a);

			offset += font.spacing * width;
		}
	}

	public static void line(VertexBuffer buffer, float x1, float y1, float x2, float y2, float w, Color color) {
		line(buffer, x1, y1, x2, y2, w, color.r, color.g, color.b, color.a);
	}

	public static void line(VertexBuffer buffer, float x1, float y1, float x2, float y2, float w, float cr, float cg, float cb, float ca) {
		float a = -1/((y1 - y2)/(x1 - x2));
		float d = (float) Math.sqrt((4 * w * w) / (1 + a * a));

		if (d == 0) {
			float uy1 = y1 - w;
			float uy2 = y2 + w;

			quad(buffer, x1, uy1, x2, uy1, x2, uy2, x1, uy2, Sprites.NONE, cr, cg, cb, ca);
			return;
		}

		// FIXME
		// this doesn't respect the winding order for some directions
		// (when the line is pointing to the left and/or downwards)
		// it would be a good idea to fix this, for now I disabled back face culling to mitigate this

		float ax1 = ((2 * x1) + d) / 2;
		float ax2 = ((2 * x1) - d) / 2;
		float bx1 = ((2 * x2) + d) / 2;
		float bx2 = ((2 * x2) - d) / 2;

		float ay1 = a * (ax1 - x1) + y1;
		float ay2 = a * (ax2 - x1) + y1;
		float by1 = a * (bx1 - x2) + y2;
		float by2 = a * (bx2 - x2) + y2;

		quad(buffer, ax1, ay1, bx1, by1, bx2, by2, ax2, ay2, Sprites.NONE, cr, cg, cb, ca);
	}

	/**
	 * Finish this frame and prepare for the next one
	 */
	@Deprecated
	public static void swap() {
		Main.window.swap();
	}

	/**
	 * Clear the screen
	 */
	public static void clear() {
		GL32.glClear(GL32.GL_COLOR_BUFFER_BIT | GL32.GL_DEPTH_BUFFER_BIT);
	}

}
