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

	public void drag(float x, float y) {
		this.offsetX += x / scaleX;
		this.offsetY += y / scaleY;
	}

	public void setZoom(float zoom) {
		this.scaleX = zoom * Main.window.height() / (float) Main.window.width();
		this.scaleY = zoom;
		this.zoom = zoom;
	}

}
