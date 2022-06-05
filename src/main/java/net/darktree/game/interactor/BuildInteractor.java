package net.darktree.game.interactor;

import net.darktree.Main;
import net.darktree.core.client.Colors;
import net.darktree.core.client.render.color.Color;
import net.darktree.core.client.render.vertex.Renderer;
import net.darktree.core.client.render.vertex.VertexBuffer;
import net.darktree.core.util.BuildingType;
import net.darktree.core.world.World;
import net.darktree.core.world.action.BuildAction;
import net.darktree.core.world.tile.TilePos;
import net.darktree.core.world.tile.TileState;
import net.darktree.game.country.Symbol;

import java.util.List;

public class BuildInteractor extends Interactor {

	private final BuildingType type;
	private final World world;

	private int x, y;
	private boolean valid;

	public BuildInteractor(BuildingType type, World world) {
		this.type = type;
		this.world = world;
	}

	boolean verify(int x, int y) {
		Symbol symbol = world.getCurrentSymbol();
		TileState[][] map = world.getTiles();

		// try matching pattern first to ensure that we don't
		// render the building outside the map
		List<TilePos> tiles = type.pattern.list(world, x, y, true);

		if (type.value > world.getCountry(symbol).getTotalMaterials()) {
			return false;
		}

		for (TilePos pos : tiles) {
			if (world.getEntity(pos.x, pos.y) != null) return false;
			if (!map[pos.x][pos.y].getTile().isReplaceable()) return false;
			if (!world.canControl(pos.x, pos.y, symbol)) return false;
		}

		return true;
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

		Renderer.quad(buffer, this.x, this.y, 2, 2, type.sprite, c.r, c.g, c.b, c.a * wave);
	}

	@Override
	public void onClick(int button, int action, int mods, int x, int y) {
		if (valid && this.x == x && this.y == y) {
			world.getManager().apply(new BuildAction(type, this.x, this.y));
			this.closed = true;
		}
	}
}
