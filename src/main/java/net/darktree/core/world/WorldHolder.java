package net.darktree.core.world;

import net.darktree.core.client.Buffers;
import net.darktree.core.client.Shaders;
import net.darktree.core.client.Sprites;
import net.darktree.core.client.Uniforms;
import net.darktree.core.client.render.pipeline.Pipeline;
import net.darktree.core.client.render.pipeline.TexturedPipeline;
import net.darktree.core.client.sound.SoundSystem;

public class WorldHolder {

	public static World world;

	public static Pipeline pipeline = new TexturedPipeline(Buffers.TEXTURED.build(), Shaders.WORLD, Sprites.ATLAS, pipeline -> {
		WorldView view = world.getView();

		Uniforms.SCALE.putFloats(view.scaleX, view.scaleY).flush();
		Uniforms.OFFSET.putFloats(view.offsetX, view.offsetY).flush();
	}, true);

	public static void draw() {
		SoundSystem.tick();
		world.draw(pipeline.buffer);
	}

}
