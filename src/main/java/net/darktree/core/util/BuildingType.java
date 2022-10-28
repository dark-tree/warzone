package net.darktree.core.util;

import net.darktree.core.client.render.image.Sprite;
import net.darktree.core.world.entity.Entity;
import net.darktree.core.world.pattern.Pattern;
import net.darktree.core.world.pattern.Patterns;

public class BuildingType extends Entity.Type {

	public final int value;
	public final int width, height;
	public final Sprite sprite;
	public final Pattern pattern;

	public BuildingType(Constructor constructor, int value, int width, int height, Sprite sprite) {
		super(constructor);

		this.value = value;
		this.width = width;
		this.height = height;
		this.sprite = sprite;
		this.pattern = Patterns.area(width, height);
	}

}
