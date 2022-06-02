package net.darktree.game.country;

import net.darktree.core.client.Sprites;
import net.darktree.core.client.render.image.Sprite;

public enum Symbol {
	NONE(null),
	CROSS("cross"),
	CIRCLE("circle"),
	TRIANGLE("triangle"),
	SQUARE("square");

	private final Sprite sprite, armored;

	Symbol(String path) {
		sprite = path == null ? null : Sprites.ATLAS.getSprite("symbol/" + path + ".png");
		armored	= path == null ? null : Sprites.ATLAS.getSprite("symbol/armored_" + path + ".png");
	}

	public Sprite getSprite() {
		return sprite;
	}

	public Sprite getArmoredSprite() {
		return armored;
	}
}
