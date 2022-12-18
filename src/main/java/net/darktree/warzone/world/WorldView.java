package net.darktree.warzone.world;

import net.darktree.warzone.Main;
import net.darktree.warzone.client.window.Input;

public class WorldView {

	public float offsetX;
	public float offsetY;
	public float scaleX;
	public float scaleY;
	public float zoom;

	protected final World world;

	public WorldView(World world) {
		this.offsetX = world.getWidth() / -2f;
		this.offsetY = world.getHeight() / -2f;
		this.world = world;

		setZoom(Input.MAP_ZOOM_MIN * 1.8f);
	}

	/**
	 * Updates the world position by moving it along the given offset, adjusted by the scale
	 */
	public void drag(float x, float y) {
		this.offsetX += x / scaleX;
		this.offsetY += y / scaleY;
	}

	/**
	 * Updates the world scale according to the given zoom level
	 */
	public void setZoom(float zoom) {
		this.scaleX = zoom * Main.window.height() / (float) Main.window.width();
		this.scaleY = zoom;
		this.zoom = zoom;
	}

}
