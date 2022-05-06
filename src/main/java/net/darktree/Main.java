package net.darktree;

import net.darktree.game.gui.PlayUserInterface;
import net.darktree.game.rendering.Buffers;
import net.darktree.game.rendering.Shaders;
import net.darktree.game.rendering.Uniforms;
import net.darktree.game.tiles.Tiles;
import net.darktree.lt2d.graphics.Input;
import net.darktree.lt2d.graphics.ScreenRenderer;
import net.darktree.lt2d.graphics.Window;
import net.darktree.lt2d.graphics.image.Image;
import net.darktree.lt2d.graphics.image.Sprite;
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

//		world.setOverlay((world1, x, y, state, color) -> {
//			if (y % 2 == 0) {
//				color.set(0.8f, 0.2f, 0.2f, 0.2f);
//			}else{
//				color.clear();
//			}
//		});

		while ( !window.shouldClose() ) {
			Renderer.clear();

			world.draw(pipeline.buffer);
			pipeline.flush();

			PlayUserInterface.draw();
			ScreenRenderer.from(0, -1).offset(0, 10).sprite(texture, Sprite.IDENTITY).box(100, 100, 100, 0).next();

			window.swap();
		}
	}

}

