package net.darktree.game.country;

import net.darktree.core.client.Sprites;
import net.darktree.core.client.render.image.Sprite;

public enum Symbol {
	NONE(null),
	CROSS("cross"),
	CIRCLE("circle"),
	TRIANGLE("triangle"),
	SQUARE("square");

	private final Sprite sprite;

	Symbol(String path) {
		sprite = path == null ? null : Sprites.ATLAS.getSprite("symbol/" + path + ".png");
	}

	public Sprite getSprite() {
		return sprite;
	}
}
