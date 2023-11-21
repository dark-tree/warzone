package net.darktree.warzone.country;

import net.darktree.warzone.client.Sprites;
import net.darktree.warzone.client.render.image.Sprite;
import net.querz.nbt.tag.CompoundTag;

public enum Symbol {
	NONE(null),
	CROSS("cross"),
	CIRCLE("circle"),
	TRIANGLE("triangle"),
	SQUARE("square");

	private final Sprite sprite, armored;

	Symbol(String path) {
		sprite = path == null ? null : Sprites.getSprite("symbol/" + path + ".png");
		armored	= path == null ? null : Sprites.getSprite("symbol/armored_" + path + ".png");
	}

	public Sprite getSprite() {
		return sprite;
	}

	public Sprite getArmoredSprite() {
		return armored;
	}

	public static Symbol fromNbt(String key, CompoundTag nbt) {
		return fromIndex(nbt.getByte(key));
	}

	public static Symbol fromIndex(byte index) {
		assert index < values().length;
		return values()[index];
	}

}
