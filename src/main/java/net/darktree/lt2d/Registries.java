package net.darktree.lt2d;

import net.darktree.game.buildings.Building;
import net.darktree.lt2d.graphics.image.Atlas;
import net.darktree.lt2d.graphics.image.Image;
import net.darktree.lt2d.graphics.image.Sprite;
import net.darktree.lt2d.util.Logger;
import net.darktree.lt2d.util.Registry;
import net.darktree.lt2d.util.Type;
import net.darktree.lt2d.world.Tile;
import net.darktree.lt2d.world.entities.Entity;

import java.util.HashMap;
import java.util.Map;

public class Registries {

	static public final Atlas ATLAS = Atlas.createEmpty();
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
		ATLAS.texture.upload();
	});

	public static Registry<Type<Entity>> ENTITIES = new Registry<>(entry -> {}, registry -> {});

	public static Registry<Type<Building>> BUILDINGS = new Registry<>(entry -> {}, registry -> {});

}
