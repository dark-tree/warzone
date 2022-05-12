package net.darktree.core.client.render.image;

import net.darktree.core.client.render.vertex.Renderer;
import net.darktree.core.client.render.vertex.VertexBuffer;
import net.darktree.core.json.FontJsonObject;
import net.darktree.core.util.Logger;
import net.darktree.core.util.Resources;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Font implements AutoCloseable {

	private final Atlas atlas;
	private final List<Sprite> sprites;
	private final float spacing;

	public Font(String bitmap, int w, int h, float spacing) {
		this.atlas = Atlas.bakedOf(Image.of(bitmap, Image.Format.RGBA));
		this.sprites = new ArrayList<>();
		this.spacing = spacing;

		Image image = this.atlas.getImage();
		int iw = image.width;
		int ih = image.height;

		if (iw % w != 0 || ih % h != 0) {
			Logger.error("Unable to load the requested image as a font of desired glyph size! (", iw, "x", ih, ") is not divisible into: (", w, "x", h, ")");
			throw new RuntimeException("Failed to load font!");
		}

		int xw = iw / w;
		int yh = ih / h;

		for (int y = 0; y < yh; y ++) {
			for (int x = 0; x < xw; x ++) {
				this.sprites.add(this.atlas.at(x * w, y * h, w, h));
			}
		}
	}

	public static Font load(String name) {
		try {
			FontJsonObject object = Resources.json("font/" + name + ".json", FontJsonObject.class);
			Font font = new Font("font/" + object.bitmap, object.x, object.y, object.separation);

			Logger.info("Loaded font: '", object.name, "'");
			return font;
		}catch (Exception e) {
			return null;
		}
	}

	public Sprite sprite(byte chr) {
		return this.sprites.get(chr);
	}

	@Deprecated
	public void draw(String text, VertexBuffer buffer, float x, float y, float size, float sizey, int r, int g, int b, int a) {
		float offset = 0;

		for (byte chr : text.getBytes(StandardCharsets.UTF_8)) {
			if (chr == '\n') {
				y -= sizey;
				offset = 0;
				continue;
			}

			Sprite sprite = this.sprite(chr);
			Renderer.quad(buffer, x + offset, y, size, sizey, sprite, r, g, b, a);

			offset += this.spacing * size;
		}
	}

	public Atlas getAtlas() {
		return atlas;
	}

	@Override
	public void close() throws Exception {
		atlas.close();
	}

}
