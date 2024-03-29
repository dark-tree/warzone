package net.darktree.warzone.client.render.color;

import net.darktree.warzone.util.Logger;
import net.darktree.warzone.util.Resources;

import java.util.HashMap;

public class ColorAtlas extends HashMap<String, float[]> {

	public static ColorAtlas load(String path) {
		try {
			return Resources.json(path, ColorAtlas.class);
		} catch (Exception e) {
			Logger.warn("Unable to load color definitions file!");
			return new ColorAtlas();
		}
	}

	public Color getColor(String name) {
		float[] values = get(name);

		if (values != null && values.length == 4) {
			return ImmutableColor.of(values[0], values[1], values[2], values[3]);
		}

		Logger.warn("Unable to load definition of color: '", name, "', it is either missing or malformed!");
		return ImmutableColor.of(0.9f, 0.0f, 0.9f, 0.5f);
	}

}
