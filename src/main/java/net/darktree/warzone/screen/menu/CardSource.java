package net.darktree.warzone.screen.menu;

import net.darktree.warzone.client.Sprites;
import net.darktree.warzone.client.render.image.Sprite;
import net.darktree.warzone.util.math.MathHelper;

import java.util.ArrayList;
import java.util.List;

public class CardSource {

	private final List<Sprite> sprites = new ArrayList<>();

	public CardSource(int max) {
		while (max --> 0) {
			sprites.add(Sprites.getSprite("gui/card/" + toPaddedInt(max, 2) + ".png"));
		}
	}

	public Sprite get() {
		return sprites.get(MathHelper.RANDOM.nextInt(sprites.size()));
	}

	private String toPaddedInt(int value, int length) {
		String source = String.valueOf(value);

		if (source.length() >= length) {
			return source;
		}

		StringBuilder builder = new StringBuilder();
		while (builder.length() < length - source.length()) {
			builder.append('0');
		}

		return builder + source;
	}

}
