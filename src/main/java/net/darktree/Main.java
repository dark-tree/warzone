package net.darktree;

import net.darktree.core.client.Buffers;
import net.darktree.core.client.Shaders;
import net.darktree.core.client.Sprites;
import net.darktree.core.client.Uniforms;
import net.darktree.core.client.render.Screen;
import net.darktree.core.client.render.image.Font;
import net.darktree.core.client.render.pipeline.Pipeline;
import net.darktree.core.client.render.pipeline.TexturedPipeline;
import net.darktree.core.client.render.vertex.Renderer;
import net.darktree.core.client.sound.SoundSystem;
import net.darktree.core.client.window.Window;
import net.darktree.core.util.Logger;
import net.darktree.core.util.Resources;
import net.darktree.core.world.World;
import net.darktree.game.screens.PlayScreen;
import net.darktree.game.tiles.Tiles;
import org.lwjgl.Version;

import java.util.Stack;

import static org.lwjgl.opengl.GL32.glClearColor;

public class Main {

	public static Stack<Screen> screens = new Stack<>();

	public static Window window;
	public static World world;

	public static Pipeline pipeline;

	public static void main(String[] args) {
		Logger.info("Current working directory: ", Resources.path());
		Logger.info("Using LWJGL ", Version.getVersion());

		long start = System.currentTimeMillis();

		window = Window.init(800, 500, "Game");

		SoundSystem.enable();

//		AudioBuffer song = SoundSystem.createBuffer("sound/test_song.ogg");
//		AudioSource source = SoundSystem.createSource(song);
//		source.setLoop(true);
//		source.setVolume(0.8f);
//		source.play();

		pipeline = new TexturedPipeline(Buffers.TEXTURED.build(), Shaders.WORLD, Sprites.ATLAS, pipeline -> {
			Uniforms.SCALE.putFloats(world.scaleX, world.scaleY).flush();
			Uniforms.OFFSET.putFloats(world.offsetX, world.offsetY).flush();
		}, true);

		// Set the clear color, evil blue from LT3D (patent pending)
		glClearColor(0.01f, 0.66f, 0.92f, 0.00f);

		world = new World(8, 8);
		world.loadTiles(pos -> Tiles.EMPTY.getDefaultVariant());

		// load and set font
		Font scribble = Font.load("scribble");

		Logger.info("System ready, took ", System.currentTimeMillis() - start, "ms!");

		screens.push(new PlayScreen(world));

		try {
			loop();
		}catch (Exception e) {
			Logger.fatal("Main thread has thrown an exception and crashed!");
			e.printStackTrace();
		}

		pipeline.close();
		window.close();
		SoundSystem.disable();
	}

	private static void loop() {
		while (!window.shouldClose()) {
			screens.forEach(Screen::draw);
			screens.removeIf(Screen::isClosed);
			Renderer.next();
		}
	}

}

