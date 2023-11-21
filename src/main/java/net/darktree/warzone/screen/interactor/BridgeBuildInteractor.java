package net.darktree.warzone.screen.interactor;

import net.darktree.warzone.Main;
import net.darktree.warzone.client.Colors;
import net.darktree.warzone.client.render.color.Color;
import net.darktree.warzone.client.render.vertex.Renderer;
import net.darktree.warzone.client.render.vertex.VertexBuffer;
import net.darktree.warzone.client.window.input.ClickEvent;
import net.darktree.warzone.world.WorldAccess;
import net.darktree.warzone.world.WorldSnapshot;
import net.darktree.warzone.world.action.BridgePlacer;
import net.darktree.warzone.world.action.BuildBridgeAction;
import net.darktree.warzone.world.entity.building.Building;
import net.darktree.warzone.world.tile.TilePos;

public class BridgeBuildInteractor extends RotatableBuildInteractor {

	private BridgePlacer bridge = null;

	public BridgeBuildInteractor(Building.Type type, WorldAccess world, boolean play) {
		super(type, world, play);
	}

	private boolean verify(int x, int y) {
		WorldSnapshot snapshot = world.getTrackingWorld();

		bridge = BridgePlacer.create(snapshot, x, y, rotation, false);
		return bridge != null && bridge.isFullyValid(snapshot.getCurrentSymbol());
	}

	@Override
	public void draw(VertexBuffer texture, VertexBuffer color) {
		int x = Main.window.input().getMouseTileX(world.getView());
		int y = Main.window.input().getMouseTileY(world.getView());

		float wave = (float) (Math.sin(Main.window.profiler.getFrameCount() / 6f) + 1) / 6;

		if (world.isPositionValid(x, y)) {
			if (!pos.equals(x, y)) {
				valid = verify(x, y);
				pos = new TilePos(x, y);
			}
		}

		if (bridge != null) {
			for (TilePos tile : bridge.getTiles()) {
				Color c = valid ? Colors.SPOT_VALID : Colors.SPOT_INVALID;
				Renderer.quad(texture, this.rotation, tile.x, tile.y, type.width, type.height, type.sprite, c.r(), c.g(), c.b(), c.a() * wave);
			}
		}
	}

	@Override
	public void onClick(ClickEvent event, int x, int y) {
		if (valid && pos.equals(x, y)) {
			world.getLedger().push(new BuildBridgeAction(pos.x, pos.y, rotation));
			this.closed = true;
		}
	}

	public void onRotated() {
		super.onRotated();
		valid = verify(pos.x, pos.y);
	}

}
