package net.darktree;

import net.darktree.core.client.Buffers;
import net.darktree.core.client.Shaders;
import net.darktree.core.client.Uniforms;
import net.darktree.core.client.render.ScreenRenderer;
import net.darktree.core.client.render.image.Font;
import net.darktree.core.client.render.image.Image;
import net.darktree.core.client.render.image.Texture;
import net.darktree.core.client.render.pipeline.Pipeline;
import net.darktree.core.client.render.vertex.Renderer;
import net.darktree.core.client.sound.AudioBuffer;
import net.darktree.core.client.sound.SoundSystem;
import net.darktree.core.client.sound.AudioSource;
import net.darktree.core.client.window.Input;
import net.darktree.core.client.window.Window;
import net.darktree.core.util.Logger;
import net.darktree.core.util.Resources;
import net.darktree.core.world.World;
import net.darktree.game.gui.PlayUserInterface;
import net.darktree.game.tiles.Tiles;
import org.lwjgl.Version;

import static org.lwjgl.opengl.GL32.glClearColor;

public class Main {

	public static Window window;
	public static World world;
	public static Texture texture;

	private static Pipeline pipeline;

	public static void main(String[] args) {
		Logger.info("Current working directory: ", Resources.path());
		Logger.info("Using LWJGL ", Version.getVersion());

		long start = System.currentTimeMillis();

		window = Window.init(800, 500, "Game");

		SoundSystem.enable();

		AudioBuffer song = SoundSystem.createBuffer("sound/test_song.ogg");

		AudioSource source = SoundSystem.createSource(song);
		source.setLoop(true);
		source.setVolume(0.8f);
		source.play();

		Input input = window.input();
		input.setZoomRange(0.07f, 1f);
		input.setGuiScale(1);

		try( Image image = Image.of("top.png", Image.Format.RGBA) ) {
			texture = image.asTexture();
			texture.upload();
		}

		pipeline = new Pipeline(Buffers.TEXTURED.build(), Shaders.WORLD, pipeline -> {
			Uniforms.SCALE.putFloats(input.scaleX, input.scaleY).flush();
			Uniforms.OFFSET.putFloats(input.offsetX, input.offsetY).flush();
		}, true);

		// Set the clear color, evil blue from LT3D (patent pending)
		glClearColor(0.01f, 0.66f, 0.92f, 0.00f);

		world = new World(8, 8);
		world.loadTiles(pos -> Tiles.EMPTY.getDefaultVariant());

		// load and set font
		Font scribble = Font.load("scribble");

		Logger.info("System ready, took ", System.currentTimeMillis() - start, "ms!");

		loop();

		pipeline.close();
		window.close();
		SoundSystem.disable();
	}

	private static void loop() {

		while ( !window.shouldClose() ) {
			world.draw(pipeline.buffer);
			pipeline.flush();

			PlayUserInterface.draw();

			ScreenRenderer.centerAt(-1, 1);
			ScreenRenderer.setOffset(0, -40);
			ScreenRenderer.text(window.profiler.getFrameRate() + " FPS", 30);

			Renderer.next();
		}
	}

}

