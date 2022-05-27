package net.darktree.core.client;

import net.darktree.core.client.render.image.Atlas;
import net.darktree.core.client.render.image.Sprite;
import net.darktree.core.util.Logger;

public class Sprites {

	public final static Atlas ATLAS = Atlas.stitchedOf("sprites");
	public final static Sprite MISSINGNO = ATLAS.getSprite("missingno.png");
	public final static Sprite NULL = ATLAS.getSprite("null.png");

	public static Sprite getSprite(String identifier) {
		try {
			return ATLAS.getSprite(identifier);
		} catch (NullPointerException e) {
			Logger.warn("Failed to load texture '", identifier, "', using missing texture!");
			return MISSINGNO;
		}
	}

	public final static Sprite TOP = getSprite("gui/top.png");
	public final static Sprite BUTTON_LEFT = getSprite("gui/button/left.png");
	public final static Sprite BUTTON_CENTER = getSprite("gui/button/center.png");
	public final static Sprite BUTTON_RIGHT = getSprite("gui/button/right.png");

	public static Sprite BUILDING_CAPITOL = getSprite("building/capitol.png");

}