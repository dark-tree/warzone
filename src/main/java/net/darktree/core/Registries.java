package net.darktree.core;

import net.darktree.core.client.render.image.Atlas;
import net.darktree.core.client.render.image.Image;
import net.darktree.core.util.Logger;
import net.darktree.core.util.Registry;
import net.darktree.core.util.Type;
import net.darktree.core.world.entity.Entity;
import net.darktree.core.world.tile.Tile;
import net.darktree.game.buildings.Building;

public class Registries {

	static public final Atlas ATLAS = new Atlas();
	static public final Image MISSINGNO = Image.of("tile/missing.png", Image.Format.RGBA);

	public static Registry<Tile> TILES = new Registry<>(entry -> {
		try {
			ATLAS.addPath("tile/" + entry.key() + ".png", entry.value());
		}catch (Exception e) {
			Logger.warn("Failed to load texture for tile '", entry.key(), "' using missing texture!");
			ATLAS.addImage(entry.value(), MISSINGNO);
		}
	}, registry -> {});

	public static Registry<Type<Entity>> ENTITIES = new Registry<>(entry -> {}, registry -> {});

	public static Registry<Type<Building>> BUILDINGS = new Registry<>(entry -> {}, registry -> {});

}
