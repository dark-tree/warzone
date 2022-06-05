package net.darktree.core.util;

import net.darktree.core.client.render.image.Sprite;
import net.darktree.core.world.Pattern;
import net.darktree.core.world.World;
import net.darktree.game.buildings.Building;

public class BuildingType {

	private final Constructor<Building> constructor;

	public final int value;
	public final Pattern pattern;
	public final Sprite sprite;

	public BuildingType(Constructor<Building> constructor, int value, Pattern pattern, Sprite sprite) {
		this.constructor = constructor;
		this.value = value;
		this.pattern = pattern;
		this.sprite = sprite;
	}

	public Building construct(World world, int x, int y) {
		return constructor.construct(world, x, y, this);
	}

	public interface Constructor<T> {
		T construct(World world, int x, int y, BuildingType type);
	}

}
