package net.darktree;

import net.darktree.game.World;
import net.darktree.opengl.Input;
import net.darktree.opengl.ScreenRenderer;
import net.darktree.opengl.Window;
import net.darktree.opengl.image.Image;
import net.darktree.opengl.image.Sprite;
import net.darktree.opengl.image.Texture;
import net.darktree.opengl.pipeline.Pipeline;
import net.darktree.opengl.shader.Program;
import net.darktree.opengl.shader.Uniform;
import net.darktree.opengl.vertex.Renderer;
import net.darktree.opengl.vertex.VertexBuffer;
import net.darktree.util.Logger;
import net.darktree.util.Resources;
import org.lwjgl.Version;

import static org.lwjgl.opengl.GL32.glClearColor;

public class Main {

	Window window;

	public void run() {
		Logger.info("Using LWJGL ", Version.getVersion());

		window = Window.init(800, 500, "Tic-Tac-Toe (Advanced) v0.1");
		Input input = window.input();

		input.setScale(1);
		input.setZoomRange(0.07f, 1f);

		loop();

		this.texture.close();
		this.pipeline.close();

		window.close();
	}

	private Pipeline pipeline;
	private Texture texture;

	private void loop() {

		Input input = window.input();

		VertexBuffer buffer = VertexBuffer.create().attribute(2).attribute(2).build();
		Program program = Program.from("shader/textured");
		Uniform scale = program.uniform("scale", Uniform.VEC2F);

		this.pipeline = new Pipeline(buffer, program, pipeline -> {
			scale.putFloat(input.scaleX).putFloat(input.scaleY).flush();
		});

//		Font font = new Font("8x8font.png", 8, 8);
//
		Sprite sprite = Sprite.IDENTITY;

		try( Image image = Image.of("grid.png", Image.Format.RGBA) ) {
			this.texture = image.asTexture(false);
			this.texture.upload();
		}

		// Set the clear color, evil blue from LT3D (patent pending)
		glClearColor(0.01f, 0.66f, 0.92f, 0.00f);

//		font.draw("Hello!", buffer, 0, 0, 0.1f);

		World world = new World(8, 8);

		while ( !window.shouldClose() ) {
			Renderer.clear();

			world.draw(this.pipeline.buffer);
			this.pipeline.flush();

			scale.putFloat(1).putFloat(1).flush();
			texture.bind();
			ScreenRenderer.from(0, -1).offset(0, 10).sprite(Sprite.IDENTITY).box(100, 100, 100, 0).next();

			window.swap();
		}
	}

	public static void main(String[] args) {
		Logger.info("Current working directory: ", Resources.path());
		new Main().run();
	}

}

