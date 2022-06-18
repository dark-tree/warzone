package net.darktree.game.interactor;

import net.darktree.Main;
import net.darktree.core.client.Colors;
import net.darktree.core.client.Sprites;
import net.darktree.core.client.render.color.Color;
import net.darktree.core.client.render.vertex.Renderer;
import net.darktree.core.client.render.vertex.VertexBuffer;
import net.darktree.core.world.World;
import net.darktree.core.world.tile.Tile;
import net.darktree.game.buildings.Building;
import net.darktree.game.country.Symbol;
import net.darktree.game.tiles.StructureTile;

public class DeconstructInteractor extends Interactor {

	private final World world;
	private final Symbol symbol;

	private int x, y;

	public DeconstructInteractor(Symbol symbol, World world) {
		this.world = world;
		this.symbol = symbol;
	}

	@Override
	public void draw(VertexBuffer buffer) {
		int x = Main.window.input().getMouseMapX(world.getView());
		int y = Main.window.input().getMouseMapY(world.getView());

		if (world.isPositionValid(x, y) && world.canControl(x, y, this.symbol)) {
			Tile tile = world.getTileState(x, y).getTile();

			float wave = (float) (Math.sin(Main.window.profiler.getFrameCount() / 6f) + 1) / 3f;
			Color c = Colors.SPOT_INVALID;

			if (tile.isDeconstructable(world, x, y) ) {
				this.x = x;
				this.y = y;

				if (tile instanceof StructureTile structure) {
					Building building = structure.getBuilding(world, x, y);

					this.x = building.x;
					this.y = building.y;

					Renderer.quad(buffer, this.x, this.y, 2, 2, Sprites.NONE, c.r, c.g, c.b, c.a * wave);
				} else {
					Renderer.quad(buffer, this.x, this.y, 1, 1, Sprites.NONE, c.r, c.g, c.b, c.a * wave);
				}
			}
		}
	}

	@Override
	public void onClick(int button, int action, int mods, int x, int y) {
		if (this.x == x && this.y == y) {
			Tile tile = world.getTileState(this.x, this.y).getTile();

			if (tile.isDeconstructable(world, this.x, this.y)) {
				tile.deconstruct(world, this.x, this.y);
			}
		}
		closed = true;
	}
}
