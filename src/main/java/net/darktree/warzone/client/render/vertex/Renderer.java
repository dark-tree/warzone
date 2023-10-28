package net.darktree.warzone.client.render.vertex;

import net.darktree.warzone.Main;
import net.darktree.warzone.client.Sprites;
import net.darktree.warzone.client.render.Alignment;
import net.darktree.warzone.client.render.color.Color;
import net.darktree.warzone.client.render.image.Font;
import net.darktree.warzone.client.render.image.Sprite;
import net.darktree.warzone.util.Direction;
import org.lwjgl.opengl.GL32;

import java.nio.charset.StandardCharsets;


public class Renderer {

	public static void overlay(VertexBuffer buffer, int x, int y, Color color) {
		quad(buffer, x, y, 1, 1, Sprites.NONE, color.r(), color.g(), color.b(), color.a());
	}

	public static void tile(VertexBuffer buffer, int x, int y, Sprite sprite) {
		quad(buffer, x, y, 1, 1, sprite, 0, 0, 0, 0);
	}

	public static void quad(VertexBuffer buffer, Direction direction, float x, float y, float w, float h, Sprite sprite, float r, float g, float b, float a) {
		quad(buffer, direction, x, y, x + w, y, x + w, y + h, x, y + h, sprite, r, g, b, a);
	}

	public static void quad(VertexBuffer buffer, float x, float y, float w, float h, Sprite sprite, float r, float g, float b, float a) {
		quad(buffer, x, y, x + w, y, x + w, y + h, x, y + h, sprite, r, g, b, a);
	}

	public static void quad(VertexBuffer buffer, Direction direction, float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4, Sprite sprite, float r, float g, float b, float a) {
		switch (direction) {
			case NORTH -> quad(buffer, x1, y1, x2, y2, x3, y3, x4, y4, sprite, r, g, b, a);
			case EAST -> quad(buffer, x2, y2, x3, y3, x4, y4, x1, y1, sprite, r, g, b, a);
			case SOUTH -> quad(buffer, x3, y3, x4, y4, x1, y1, x2, y2, sprite, r, g, b, a);
			case WEST -> quad(buffer, x4, y4, x1, y1, x2, y2, x3, y3, sprite, r, g, b, a);
		}
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
		float offset = alignment.getOffset((text.length() /* - 1*/) * width * font.spacing);

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
		line(buffer, x1, y1, x2, y2, w, color.r(), color.g(), color.b(), color.a());
	}

	public static void line(VertexBuffer buffer, float x1, float y1, float x2, float y2, float w, float cr, float cg, float cb, float ca) {
		float vx = x2 - x1;
		float vy = y2 - y1;

		float len = (float) Math.sqrt(vy * vy + vx * vx) * (2 / w);
		float nx =   vy / len;
		float ny = - vx / len;

		float r1x = x1 + nx;
		float r1y = y1 + ny;
		float r2x = x1 - nx;
		float r2y = y1 - ny;
		float r3x = x2 + nx;
		float r3y = y2 + ny;
		float r4x = x2 - nx;
		float r4y = y2 - ny;

		quad(buffer, r1x, r1y, r2x, r2y, r4x, r4y, r3x, r3y, Sprites.NONE, cr, cg, cb, ca);
	}

	/**
	 * Finish this frame and prepare for the next one
	 */
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
