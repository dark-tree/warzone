package net.darktree.core;

import net.darktree.core.network.Packet;
import net.darktree.core.util.BuildingType;
import net.darktree.core.util.Registry;
import net.darktree.core.util.Type;
import net.darktree.core.world.entity.Entity;
import net.darktree.core.world.tile.SpriteBridge;
import net.darktree.core.world.tile.Tile;

public class Registries {

	public static Registry<Tile> TILES = new Registry<>(entry -> SpriteBridge.register(entry.value(), entry.key()));

	public static Registry<Type<Entity>> ENTITIES = new Registry<>(entry -> {});

	public static Registry<BuildingType> BUILDINGS = new Registry<>(entry -> {});

	public static Registry<Packet> PACKETS = new Registry<>(entry -> {});

}
