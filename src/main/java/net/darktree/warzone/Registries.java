package net.darktree.warzone;

import net.darktree.warzone.network.Packet;
import net.darktree.warzone.util.Registry;
import net.darktree.warzone.world.action.manager.Action;
import net.darktree.warzone.world.entity.Entity;
import net.darktree.warzone.world.tile.SpriteBridge;
import net.darktree.warzone.world.tile.Tile;

public class Registries {
	public static Registry<Tile> TILES = new Registry<>(entry -> SpriteBridge.register(entry.value(), entry.key()));
	public static Registry<Entity.Type> ENTITIES = new Registry<>();
	public static Registry<Packet> PACKETS = new Registry<>();
	public static Registry<Action.Type> ACTIONS = new Registry<>();
}
