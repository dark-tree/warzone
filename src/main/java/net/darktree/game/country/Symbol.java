package net.darktree.game.country;

import net.darktree.core.Registries;
import net.darktree.core.client.render.image.Sprite;

public enum Symbol {
	NONE(null),
	CROSS("cross"),
	CIRCLE("circle"),
	TRIANGLE("triangle"),
	SQUARE("square");

	Symbol(String path) {
		if (path != null) Registries.ATLAS.addPath("tile/" + path + ".png", this);
	}

	public Sprite getSprite() {
		return Registries.ATLAS.getSprite(this);
	}
}
