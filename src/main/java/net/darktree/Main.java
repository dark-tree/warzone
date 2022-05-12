package net.darktree;

import net.darktree.game.gui.PlayUserInterface;
import net.darktree.game.rendering.Buffers;
import net.darktree.game.rendering.Shaders;
import net.darktree.game.rendering.Uniforms;
import net.darktree.game.tiles.Tiles;
import net.darktree.lt2d.graphics.Input;
import net.darktree.lt2d.graphics.ScreenRenderer;
import net.darktree.lt2d.graphics.Window;
import net.darktree.lt2d.graphics.image.Font;
import net.darktree.lt2d.graphics.image.Image;
import net.darktree.lt2d.graphics.image.Texture;
import net.darktree.lt2d.graphics.pipeline.Pipeline;
import net.darktree.lt2d.graphics.vertex.Renderer;
import net.darktree.lt2d.util.Logger;
import net.darktree.lt2d.util.Resources;
import net.darktree.lt2d.world.World;
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
		world.loadTiles(pos -> Tiles.EMPTY.getDefaultVariant());

		world.addEntity(0, 0, Tiles.TEST);

		Font font = Font.load("scribble");

//		ScreenRenderer.fromMouse().box(100, 100).sprite(font)

		while ( !window.shouldClose() ) {
			Renderer.clear();

			world.draw(pipeline.buffer);
			pipeline.flush();

			PlayUserInterface.draw();

			StringBuilder string = new StringBuilder();

			for(char c = 1; c <= '~'; c ++) {
				string.append(c);
			}

			font.draw("\n\nWarzone WARZONE \nQUICK BROWN FOX JUMPS OVER THE LAZY DOG? \nQuick Brown Fox Jumps Over The Lazy Dog! \nquick brown fox jumps over the lazy dog!" + string, pipeline.buffer, 0, 0, 1, 1, 1, 1, 1, 0);
			pipeline.flush();

//			ScreenRenderer.fromMouse().text(font, "\n\nWarzone WARZONE \nQUICK BROWN FOX JUMPS OVER THE LAZY DOG? \nQuick Brown Fox Jumps Over The Lazy Dog! \nquick brown fox jumps over the lazy dog!" + string, 50, 0, 0, 0, 1).endText();

			window.swap();
		}
	}

}

