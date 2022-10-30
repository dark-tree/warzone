package net.darktree.warzone.client.render.color;

import net.darktree.warzone.util.Logger;
import net.darktree.warzone.util.Resources;

import java.io.IOException;
import java.util.HashMap;

public class ColorAtlas extends HashMap<String, float[]> {
	public static ColorAtlas load(String path) {
		try {
			return Resources.json(path, ColorAtlas.class);
		} catch (IOException e) {
			Logger.warn("Unable to load color definitions file!");
			return new ColorAtlas();
		}
	}

	public Color getColor(String name) {
		float[] values = get(name);

		if (values != null && values.length == 4) {
			return new Color(values[0], values[1], values[2], values[3]);
		}

		Logger.warn("Unable to load definition for color: '", name, "', it is either missing or malformed!");
		return new Color(0.9f, 0.0f, 0.9f, 0.5f);
	}
}
