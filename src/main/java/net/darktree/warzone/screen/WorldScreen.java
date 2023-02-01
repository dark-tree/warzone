package net.darktree.warzone.screen;

import net.darktree.warzone.client.render.Screen;
import net.darktree.warzone.client.render.WorldBuffers;
import net.darktree.warzone.world.World;

public class WorldScreen extends Screen {

	protected final World world;
	protected final WorldBuffers buffers;

	protected WorldScreen(World world) {
		this.world = world;
		this.buffers = new WorldBuffers(world.getView());
	}

	@Override
	public void draw(boolean focused) {
		this.world.getView().draw(buffers);
	}

	@Override
	public void close() {
		super.close();
		this.buffers.close();
	}

}
