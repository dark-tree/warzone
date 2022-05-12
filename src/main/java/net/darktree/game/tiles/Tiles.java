package net.darktree.game.tiles;

import net.darktree.game.buildings.Building;
import net.darktree.game.country.Symbol;
import net.darktree.game.entities.UnitEntity;
import net.darktree.core.Registries;
import net.darktree.core.client.render.image.Sprite;
import net.darktree.core.util.Type;
import net.darktree.core.world.tile.Tile;
import net.darktree.core.world.entity.Entity;

public class Tiles {
	public static Tile EMPTY = Registries.TILES.register("empty", new EmptyTile());
	public static Tile MATERIAL = Registries.TILES.register("material_ore", new MaterialOreTile());
	public static Tile MATERIAL_MINE = Registries.TILES.register("material_mine", new MaterialMineTile());
	public static Tile WATER = Registries.TILES.register("water", new WaterTile());
	public static Tile STRUCTURE = Registries.TILES.register("structure", new StructureTile());

	public static Type<Entity> TEST = Registries.ENTITIES.register("circle", new Type<>(UnitEntity::new));

	public static Type<Building> BUILD = Registries.BUILDINGS.register("build", new Type<>(TestBuilding::new));

	public static Sprite BASIC_TEST_BUILD;

	static {
		var ref = Registries.ATLAS.add("tile/center-build.png");

		Symbol.values(); // load class
		Registries.TILES.freeze();

		BASIC_TEST_BUILD = ref.sprite();
	}
}
