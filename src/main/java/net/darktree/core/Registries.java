package net.darktree.core;

import net.darktree.core.client.render.image.Atlas;
import net.darktree.core.client.render.image.Image;
import net.darktree.core.client.render.image.Sprite;
import net.darktree.core.util.Logger;
import net.darktree.core.util.Registry;
import net.darktree.core.util.Type;
import net.darktree.core.world.entity.Entity;
import net.darktree.core.world.tile.Tile;
import net.darktree.game.buildings.Building;

import java.util.HashMap;
import java.util.Map;

public class Registries {

	static public final Atlas ATLAS = new Atlas();
	static public final Map<String, Sprite> TILE_SPRITES = new HashMap<>();

	static public final Image MISSINGNO = Image.of("tile/missing.png", Image.Format.RGBA);

	public static Registry<Tile> TILES = new Registry<>(entry -> {
		entry.value().setName(entry.key());
		try {
			ATLAS.add("tile/" + entry.key() + ".png", entry.key());
		}catch (Exception e) {
			Logger.warn("Failed to load texture for tile '", entry.key(), "' using missing texture!");
			ATLAS.add(entry.key(), MISSINGNO);
		}
	}, registry -> {
		ATLAS.freeze().forEach(entry -> {
			TILE_SPRITES.put(entry.getKey(), entry.getValue());
		});
	});

	public static Registry<Type<Entity>> ENTITIES = new Registry<>(entry -> {}, registry -> {});

	public static Registry<Type<Building>> BUILDINGS = new Registry<>(entry -> {}, registry -> {});

}
