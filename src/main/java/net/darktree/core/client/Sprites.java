package net.darktree.core.client;

import net.darktree.core.client.render.image.Atlas;
import net.darktree.core.client.render.image.Sprite;
import net.darktree.core.util.Logger;

public class Sprites {

	public final static Atlas ATLAS = Atlas.of("sprites");
	public final static Sprite MISSINGNO = ATLAS.getSprite("missingno.png");

	public static Sprite getSprite(String identifier) {
		try {
			return ATLAS.getSprite(identifier);
		} catch (NullPointerException e) {
			Logger.warn("Failed to load texture '", identifier, "', using missing texture!");
			return MISSINGNO;
		}
	}

	public final static Sprite TOP = getSprite("gui/top.png");
	public static Sprite BASIC_TEST_BUILD = getSprite("tile/center-build.png");

}
