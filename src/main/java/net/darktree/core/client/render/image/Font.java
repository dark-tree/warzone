package net.darktree.core.client.render.image;

import net.darktree.core.client.render.ScreenRenderer;
import net.darktree.core.json.FontJsonObject;
import net.darktree.core.util.Logger;
import net.darktree.core.util.Resources;

import java.util.ArrayList;
import java.util.List;

public class Font implements AutoCloseable {

	private final Atlas atlas;
	private final List<Sprite> sprites;
	public final float spacing;

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

			ScreenRenderer.registerFontPipeline(font);
			ScreenRenderer.setFont(font);

			Logger.info("Loaded font: '", object.name, "'");
			return font;
		}catch (Exception e) {
			return null;
		}
	}

	public Sprite sprite(byte chr) {
		return this.sprites.get(chr);
	}

	public Atlas getAtlas() {
		return atlas;
	}

	@Override
	public void close() throws Exception {
		atlas.close();
	}

}
