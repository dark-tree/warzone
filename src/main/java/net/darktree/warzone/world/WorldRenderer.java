package net.darktree.warzone.world;

import net.darktree.warzone.client.Colors;
import net.darktree.warzone.client.render.WorldBuffers;
import net.darktree.warzone.client.render.vertex.Renderer;
import net.darktree.warzone.client.render.vertex.VertexBuffer;
import net.darktree.warzone.country.Symbol;
import net.darktree.warzone.util.Logger;
import net.darktree.warzone.world.overlay.Overlay;

public class WorldRenderer extends WorldView {

	private Overlay overlay = null;
	private boolean redrawSurface = true;
	private boolean redrawBuildings = true;

	public WorldRenderer(World world) {
		super(world);
	}

	public void draw(WorldBuffers buffers) {
		world.update();

		if (redrawSurface || redrawBuildings) {
			markOverlayDirty();
		}

		final int width = world.getWidth();
		final int height = world.getHeight();

		if (redrawSurface) {
			buffers.getSurface().clear();

			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					world.getTileState(x, y).getTile().draw(x, y, buffers.getSurface());
					drawBorders(buffers.getSurface(), x, y);
				}
			}

			Logger.info("Surface redrawn, using " + buffers.getSurface().count() + " vertices");
		}

		if (redrawBuildings) {
			buffers.getBuilding().clear();
		}

		world.getEntities().forEach(entity -> entity.draw(buffers, redrawBuildings));

		if (overlay != null) {
			VertexBuffer buffer = buffers.getOverlay();

			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					Renderer.overlay(buffer, x, y, overlay.getColor(world, x, y, world.getTileState(x, y)));
				}
			}
		}

		redrawSurface = false;
		redrawBuildings = false;
	}


	private void drawBorders(VertexBuffer buffer, int x, int y) {
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

	/**
	 * Marks the overlay as outdated
	 */
	public void markOverlayDirty() {
		if (overlay != null) overlay.markDirty();
	}

	/**
	 * Marks the world surface as outdated,
	 * called after a tile or border update
	 */
	public void markSurfaceDirty() {
		redrawSurface = true;
	}

	/**
	 * Marks the world building layer as outdated,
	 * called placing or destroying a building or structure
	 */
	public void markBuildingsDirty() {
		redrawBuildings = true;
	}

}
