package net.darktree.game.country;

import net.darktree.lt2d.Registries;
import net.darktree.lt2d.graphics.image.Atlas;
import net.darktree.lt2d.graphics.image.Sprite;

public enum Symbol {
	NONE(null),
	CROSS("cross"),
	CIRCLE("circle"),
	TRIANGLE("triangle"),
	SQUARE("square");

	private final Atlas.SpriteReference ref;

	Symbol(String path) {
		ref = (path != null) ? Registries.ATLAS.add("tile/" + path + ".png") : null;
	}

	public Sprite getSprite() {
		return ref.sprite();
	}
}
