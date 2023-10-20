package net.darktree.warzone.screen.interactor;

import net.darktree.warzone.Main;
import net.darktree.warzone.client.Colors;
import net.darktree.warzone.client.render.color.Color;
import net.darktree.warzone.client.render.vertex.Renderer;
import net.darktree.warzone.client.render.vertex.VertexBuffer;
import net.darktree.warzone.client.window.input.ClickEvent;
import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.util.Direction;
import net.darktree.warzone.world.World;
import net.darktree.warzone.world.action.BuildAction;
import net.darktree.warzone.world.entity.building.Building;
import net.darktree.warzone.world.tile.Surface;
import net.darktree.warzone.world.tile.Tile;
import net.darktree.warzone.world.tile.TilePos;
import net.darktree.warzone.world.tile.TileState;

import java.util.List;

public class BuildInteractor extends PlacementInteractor {

	protected Direction rotation = Direction.NORTH;

	public BuildInteractor(Building.Type type, World world, boolean play) {
		super(type, world, play);
	}

	private boolean verify(int x, int y) {
		Symbol symbol = world.getCurrentSymbol();

		// try matching pattern first to ensure that we don't
		// render the building outside the map
		List<TilePos> tiles = type.pattern.list(world, x, y, true);

		if (type.value > world.getCountry(symbol).getTotalMaterials()) {
			return false;
		}

		for (TilePos pos : tiles) {
			TileState state = world.getTileState(pos);
			Tile tile = state.getTile();

			if (state.getEntity() != null) return false;
			if (state.getOwner() != symbol || !world.canControl(pos.x, pos.y)) return false;
			if (tile.getSurface() != Surface.LAND || !tile.canStayOn()) return false;
		}

		return true;
	}

	@Override
	public void draw(VertexBuffer texture, VertexBuffer color) {
		int x = Main.window.input().getMouseTileX(world.getView());
		int y = Main.window.input().getMouseTileY(world.getView());

		float wave = (float) (Math.sin(Main.window.profiler.getFrameCount() / 6f) + 1) / 6;

		if (world.isPositionValid(x, y)) {
			if (!pos.equals(x, y)) {
				try{
					valid = verify(x, y);
					pos = new TilePos(x, y);
				}catch (Exception ignore){
					// pattern did not match
				}
			}
		}

		Color c = valid ? Colors.SPOT_VALID : Colors.SPOT_INVALID;

		Renderer.quad(texture, this.rotation, pos.x, pos.y, type.width, type.height, type.sprite, c.r, c.g, c.b, c.a * wave);
	}

	@Override
	public void onClick(ClickEvent event, int x, int y) {
		if (valid && pos.equals(x, y)) {
			world.getManager().apply(new BuildAction(world, type, pos.x, pos.y, rotation));
			this.closed = true;
		}
	}

}
