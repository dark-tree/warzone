package net.darktree.opengl.image;

import net.darktree.util.Logger;
import net.darktree.opengl.vertex.VertexBuffer;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Font {

	private final Atlas atlas;
	private final List<Sprite> sprites;

	public Font(String font, int w, int h) {

		this.atlas = Atlas.createBaked(Image.of(font, Image.Format.RGBA));
		this.sprites = new ArrayList<>();

		int iw = this.atlas.image.width();
		int ih = this.atlas.image.height();

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

	public Sprite sprite(byte chr) {
		return this.sprites.get(chr);
	}

	public void draw(String text, VertexBuffer buffer, float x, float y, float size) {
		float offset = 0;

		this.atlas.texture.bind();
		this.atlas.texture.upload();

		for (byte chr : text.getBytes(StandardCharsets.UTF_8)) {
			Sprite sprite = this.sprite(chr);

			vertex(buffer, x + offset, y, sprite.u1(), sprite.v1());
			vertex(buffer, x + offset + size, y, sprite.u2(), sprite.v1());
			vertex(buffer, x + offset, y + size, sprite.u1(), sprite.v2());

			vertex(buffer, x + offset + size, y, sprite.u2(), sprite.v1());
			vertex(buffer, x + offset + size, y + size, sprite.u2(), sprite.v2());
			vertex(buffer, x + offset, y + size, sprite.u1(), sprite.v2());

			offset += size;
		}
	}

	private void vertex(VertexBuffer buffer, float x, float y, float u, float v) {
		buffer.putFloat(x).putFloat(y).putFloat(u).putFloat(v);
	}

}
