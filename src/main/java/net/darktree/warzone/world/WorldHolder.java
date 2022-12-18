package net.darktree.warzone.world;

import net.darktree.warzone.client.render.WorldBuffers;
import net.darktree.warzone.client.sound.SoundSystem;

public class WorldHolder {

	public static World world;
	public static WorldBuffers buffers;

	public static void setWorld(World world) {
		if (WorldHolder.buffers != null) {
			WorldHolder.buffers.close();
		}

		WorldHolder.world = world;
		WorldHolder.buffers = new WorldBuffers(world.getView());
	}

	public static void draw() {
		SoundSystem.tick();
		world.getView().draw(buffers);
	}

}
