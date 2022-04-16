package net.darktree.lt2d;

import net.darktree.lt2d.graphics.image.Atlas;
import net.darktree.lt2d.graphics.image.Sprite;
import net.darktree.lt2d.util.Registry;
import net.darktree.lt2d.world.Entity;
import net.darktree.lt2d.world.Tile;

import java.util.HashMap;
import java.util.Map;

public class Registries {

	static public final Atlas ATLAS = Atlas.createEmpty();
	static public final Map<String, Sprite> TILE_SPRITES = new HashMap<>();

	public static Registry<Tile> TILES = new Registry<>(entry -> {
		entry.value().setName(entry.key());
		ATLAS.add("tile/" + entry.key() + ".png", entry.key());
	}, registry -> {
		ATLAS.freeze().forEach(entry -> {
			TILE_SPRITES.put(entry.getKey(), entry.getValue());
		});
		ATLAS.texture.upload();
	});

	public static Registry<Entity.Type<?>> ENTITIES = new Registry<>(entry -> {}, registry -> {});

}
