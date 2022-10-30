package net.darktree.warzone.world.tile;

import net.darktree.warzone.client.Sprites;
import net.darktree.warzone.client.render.image.Sprite;
import net.darktree.warzone.util.Logger;

import java.util.IdentityHashMap;
import java.util.Map;

public class SpriteBridge {

	private static final Map<Tile, Sprite> SPRITES = new IdentityHashMap<>();

	public static void register(Tile tile, String name) {
		try {
			SPRITES.put(tile, Sprites.ATLAS.getSprite("tile/" + name + ".png"));
		} catch (Exception e) {
			Logger.warn("Failed to load texture for tile '", name, "' using missing texture!");
			SPRITES.put(tile, Sprites.MISSINGNO);
		}
	}

	public static Sprite getSprite(Tile tile) {
		return SPRITES.get(tile);
	}

}
