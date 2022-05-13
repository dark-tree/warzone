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

	private static Pipeline pipeline;

	public static void main(String[] args) {
		Logger.info("Current working directory: ", Resources.path());
		Logger.info("Using LWJGL ", Version.getVersion());

		window = Window.init(800, 500, "Tic-Tac-Toe (Advanced) v0.1");

		Input input = window.input();
		input.setZoomRange(0.07f, 1f);
		input.setGuiScale(1);

		loop();

		pipeline.close();
		window.close();
	}

	public static Texture texture;

	private static void loop() {

		Input input = window.input();

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

		world.addEntity(0, 0, Tiles.TEST);

		// load and set font
		Font scribble = Font.load("scribble");

		while ( !window.shouldClose() ) {
			world.draw(pipeline.buffer);
			pipeline.flush();

			PlayUserInterface.draw();

			ScreenRenderer.centerAt(-1, 1);
			ScreenRenderer.setOffset(0, -40);
			ScreenRenderer.text(window.profiler.getFrameRate() + " FPS (" + String.format("%.2f", window.profiler.getFrameTime()/1000000f) + " ms)", 30);

			Renderer.next();
		}
	}

}

