package net.darktree.warzone.world;

import net.darktree.warzone.client.Colors;
import net.darktree.warzone.client.render.WorldBuffers;
import net.darktree.warzone.client.render.vertex.Renderer;
import net.darktree.warzone.client.render.vertex.VertexBuffer;
import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.util.Logger;
import net.darktree.warzone.world.overlay.Overlay;

public class WorldRenderer extends WorldView {

	private WorldAccess access;
	private Overlay overlay = null;

	public WorldRenderer(WorldAccess access) {
		super(access.getInfo());
		this.access = access;
	}

	public void draw(WorldBuffers buffers) {
		int flags = access.pullRenderBits();
		boolean buildings = (flags & Update.BUILDING) != 0;

		if (0 != (flags & Update.OVERLAY) && overlay != null) {
			overlay.markDirty();
		}

		WorldInfo info = access.getInfo();
		WorldSnapshot snapshot = access.getTrackingWorld();

		if ((flags & Update.SURFACE) != 0) {
			buffers.getSurface().clear();

			for (int x = 0; x < info.width; x++) {
				for (int y = 0; y < info.height; y++) {
					snapshot.getTileState(x, y).getTile().draw(x, y, buffers.getSurface());
					drawBorders(buffers.getSurface(), x, y);
				}
			}

			Logger.info("Surface redrawn, using " + buffers.getSurface().count() + " vertices");
		}

		if (buildings) {
			buffers.getBuilding().clear();
		}

		snapshot.getEntities().forEach(entity -> entity.draw(buffers, buildings));

		if (overlay != null) {
			VertexBuffer buffer = buffers.getOverlay();

			for (int x = 0; x < info.width; x++) {
				for (int y = 0; y < info.height; y++) {
					Renderer.overlay(buffer, x, y, overlay.getColor(snapshot, x, y, snapshot.getTileState(x, y)));
				}
			}
		}
	}


	private void drawBorders(VertexBuffer buffer, int x, int y) {
		WorldSnapshot world = access.getTrackingWorld();

		Symbol self = world.getTileState(x, y).getOwner();
		float w = 0.03f;

		if (x != 0 && world.getTileState(x - 1, y).getOwner() != self) {
			Renderer.line(buffer, x, y, x, y + 1, w, Colors.BORDER);
		}

		if (y != 0 && world.getTileState(x, y - 1).getOwner() != self) {
			Renderer.line(buffer, x, y, x + 1, y, w, Colors.BORDER);
		}
	}

	/**
	 * Sets a new world overlay
	 */
	public void setOverlay(Overlay overlay) {
		this.overlay = overlay;
	}

	/**
	 * Removes an overlay, if present
	 */
	public void hideOverlay() {
		this.overlay = null;
	}

}
