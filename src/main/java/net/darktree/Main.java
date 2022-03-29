package net.darktree;

import net.darktree.game.World;
import net.darktree.game.rendering.Buffers;
import net.darktree.game.rendering.Shaders;
import net.darktree.game.rendering.Uniforms;
import net.darktree.opengl.Input;
import net.darktree.opengl.ScreenRenderer;
import net.darktree.opengl.Window;
import net.darktree.opengl.image.Image;
import net.darktree.opengl.image.Sprite;
import net.darktree.opengl.image.Texture;
import net.darktree.opengl.pipeline.Pipeline;
import net.darktree.opengl.vertex.Renderer;
import net.darktree.util.Logger;
import net.darktree.util.Resources;
import org.lwjgl.Version;

import static org.lwjgl.opengl.GL32.glClearColor;

public class Main {

	Window window;
	public static World world;

	public void run() {
		Logger.info("Using LWJGL ", Version.getVersion());

		window = Window.init(800, 500, "Tic-Tac-Toe (Advanced) v0.1");

		Input input = window.input();
		input.setZoomRange(0.07f, 1f);
		input.setGuiScale(1);

		loop();

		pipeline.close();
		window.close();
	}

	private Pipeline pipeline;

	private void loop() {

		Input input = window.input();
		Texture texture;

		try( Image image = Image.of("grid.png", Image.Format.RGBA) ) {
			texture = image.asTexture(false);
			texture.upload();
		}

		pipeline = new Pipeline(Buffers.TEXTURED.build(), Shaders.TEXTURED, pipeline -> {
			Uniforms.SCALE.putFloats(input.scaleX, input.scaleY).flush();
			Uniforms.OFFSET.putFloats(input.offsetX, input.offsetY).flush();
		});

		// Set the clear color, evil blue from LT3D (patent pending)
		glClearColor(0.01f, 0.66f, 0.92f, 0.00f);

		world = new World(8, 8);

		while ( !window.shouldClose() ) {
			Renderer.clear();

			world.draw(this.pipeline.buffer);
			this.pipeline.flush();

			ScreenRenderer.from(0, -1).offset(0, 10).sprite(texture, Sprite.IDENTITY).box(100, 100, 100, 0).next();

			window.swap();
		}
	}

	public static void main(String[] args) {
		Logger.info("Current working directory: ", Resources.path());
		new Main().run();
	}

}

