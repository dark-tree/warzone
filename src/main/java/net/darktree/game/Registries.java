package net.darktree.game;

public class Registries {
	// TODO ask the factory for a list of sprites to load
	public static Registry<Tile.Factory> TILES = new Registry<>(entry -> {
		//entry.value().requestSprites();
	});
}
