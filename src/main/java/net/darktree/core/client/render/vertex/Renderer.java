package net.darktree.core.client.render.vertex;

import net.darktree.Main;
import net.darktree.core.client.render.image.Font;
import net.darktree.core.client.render.image.Sprite;
import net.darktree.core.util.Color;
import net.darktree.core.world.overlay.Overlay;
import net.darktree.core.world.tile.TileState;
import org.lwjgl.opengl.GL32;

import java.nio.charset.StandardCharsets;


public class Renderer {

	private final static Color COLOR = new Color();

	public static void tile(VertexBuffer buffer, int x, int y, TileState state, Sprite sprite) {
		Overlay overlay = Main.world.getOverlay();

		if (overlay != null) {
			overlay.getColor(Main.world, x, y, state, COLOR);
		}else{
			COLOR.clear();
		}

		quad(buffer, x, y, 1, 1, sprite, COLOR.r, COLOR.g, COLOR.b, COLOR.a);
	}

	public static void quad(VertexBuffer buffer, float x, float y, float w, float h, Sprite sprite, float r, float g, float b, float a) {
		vertex(buffer, x, y, sprite.u1(), sprite.v1(), r, g, b, a);
		vertex(buffer, x + w, y, sprite.u2(), sprite.v1(), r, g, b, a);
		vertex(buffer, x, y + h, sprite.u1(), sprite.v2(), r, g, b, a);

		vertex(buffer, x + w, y, sprite.u2(), sprite.v1(), r, g, b, a);
		vertex(buffer, x + w, y + h, sprite.u2(), sprite.v2(), r, g, b, a);
		vertex(buffer, x, y + h, sprite.u1(), sprite.v2(), r, g, b, a);
	}

	public static void vertex(VertexBuffer buffer, float x, float y, float u, float v, float r, float g, float b, float a) {
		buffer.putFloat(x).putFloat(y).putFloat(u).putFloat(v).putFloat(r).putFloat(g).putFloat(b).putFloat(a);
	}

	public static void text(String text, Font font, VertexBuffer buffer, float x, float y, float width, float height, float r, float g, float b, float a) {
		float offset = 0;

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

	public static void clear() {
		GL32.glClear(GL32.GL_COLOR_BUFFER_BIT | GL32.GL_DEPTH_BUFFER_BIT);
	}

}
