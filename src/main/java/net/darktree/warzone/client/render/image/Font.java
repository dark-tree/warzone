package net.darktree.warzone.client.render.image;

import net.darktree.warzone.client.json.FontJsonBlob;
import net.darktree.warzone.client.render.ScreenRenderer;
import net.darktree.warzone.util.Logger;
import net.darktree.warzone.util.Resources;

import java.util.ArrayList;
import java.util.List;

public class Font implements AutoCloseable, TextureConvertible {

	private final Atlas atlas;
	private final List<Sprite> sprites;
	public final float spacing;

	protected Font(String bitmap, int w, int h, float spacing) {
		Image image = Image.of(bitmap, Image.Format.RGBA);

		this.atlas = Atlas.identityOf(image);
		this.sprites = new ArrayList<>();
		this.spacing = spacing;

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

	/**
	 * Load a font as described by font JSON definition
	 */
	public static Font load(String name) {
		try {
			FontJsonBlob object = Resources.json("font/" + name + ".json", FontJsonBlob.class);
			Font font = new Font("font/" + object.bitmap, object.x, object.y, object.separation);

			ScreenRenderer.registerFontPipeline(font);
			Logger.info("Loaded font: '", object.name, "'");
			return font;
		}catch (Exception e) {
			return null;
		}
	}

	/**
	 * Get a sprite for the given character
	 */
	public Sprite sprite(byte chr) {
		return this.sprites.get(chr);
	}

	@Override
	public void close() throws Exception {
		atlas.close();
	}

	@Override
	public Texture getTexture() {
		return atlas.getTexture();
	}
}
