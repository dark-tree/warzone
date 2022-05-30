package net.darktree.game.interactor;

import net.darktree.Main;
import net.darktree.core.client.Colors;
import net.darktree.core.client.Sprites;
import net.darktree.core.client.render.color.Color;
import net.darktree.core.client.render.vertex.Renderer;
import net.darktree.core.client.render.vertex.VertexBuffer;
import net.darktree.core.util.Type;
import net.darktree.core.world.World;
import net.darktree.core.world.action.BuildAction;
import net.darktree.core.world.tile.TilePos;
import net.darktree.core.world.tile.TileState;
import net.darktree.game.buildings.Building;
import net.darktree.game.country.Symbol;
import net.darktree.game.tiles.Tiles;

import java.util.List;

public class BuildInteractor extends Interactor {

	private final Type<Building> type;
	private final World world;

	private int x, y;
	private Building building;
	private boolean valid;

	public BuildInteractor(Type<Building> type, World world) {
		this.type = type;
		this.world = world;
	}

	boolean verify(int x, int y) {
		Symbol symbol = world.getCurrentSymbol();
		this.building = type.construct(world, x, y);

		if (building.getCost() > world.getCountry(symbol).getTotalMaterials()) {
			return false;
		}

		List<TilePos> tiles = building.getPattern().list(world, x, y, true);
		TileState[][] map = world.getTiles();

		return tiles.stream().filter(pos -> world.getEntity(pos.x, pos.y) == null).map(pos -> map[pos.x][pos.y]).allMatch(state -> state.getTile().isReplaceable() && state.getOwner() == symbol);
	}

	@Override
	public void draw(VertexBuffer buffer) {
		int x = Main.window.input().getMouseMapX(world.getView());
		int y = Main.window.input().getMouseMapY(world.getView());

		float wave = (float) (Math.sin(Main.window.profiler.getFrameCount() / 6f) + 1) / 6;

		if (world.isPositionValid(x, y)) {
			if (this.x != x || this.y != y) {
				try{
					valid = verify(x, y);
					this.x = x;
					this.y = y;
				}catch (Exception ignore){
					// pattern did not match
				}
			}
		}

		Color c = valid ? Colors.SPOT_VALID : Colors.SPOT_INVALID;

		Renderer.quad(buffer, this.x, this.y, 2, 2, Sprites.BUILDING_CAPITOL, c.r, c.g, c.b, c.a * wave);
	}

	@Override
	public void onClick(int button, int action, int mods, int x, int y) {
		if (valid && this.x == x && this.y == y) {
			world.getManager().apply(new BuildAction(Tiles.BUILD, this.x, this.y));
		}
	}
}
