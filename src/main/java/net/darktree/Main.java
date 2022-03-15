package net.darktree;

import net.darktree.game.World;
import net.darktree.opengl.Input;
import net.darktree.opengl.Window;
import net.darktree.opengl.image.Image;
import net.darktree.opengl.image.Sprite;
import net.darktree.opengl.image.Texture;
import net.darktree.opengl.shader.Program;
import net.darktree.opengl.shader.Uniform;
import net.darktree.opengl.vertex.Renderer;
import net.darktree.opengl.vertex.VertexBuffer;
import net.darktree.util.Logger;
import net.darktree.util.Resources;
import org.lwjgl.Version;
import org.lwjgl.opengl.GL32;

import static org.lwjgl.opengl.GL11.*;

public class Main {

	Window window;

	public void run() {
		Logger.info("Using LWJGL ", Version.getVersion());

		window = Window.init(800, 500, "Hello!");
		Input input = window.input();

		input.setScale(1);
		input.setZoomRange(0.07f, 1f);

		loop();

		this.texture.close();
		this.buffer.close();
		this.program.close();

		window.close();
	}

	private Program program;
	private VertexBuffer buffer;
	private Texture texture;

	private void loop() {

		Program.Builder programBuilder = Program.create();
		programBuilder.add("shader/textured.frag", GL32.GL_FRAGMENT_SHADER);
		programBuilder.add("shader/textured.vert", GL32.GL_VERTEX_SHADER);
		this.program = programBuilder.link();
		this.program.bind();

		// setting a vec2f uniform called scale
		Uniform scale = program.uniform("scale", Uniform.VEC2F);

		VertexBuffer.Builder bufferBuilder = VertexBuffer.create();
		bufferBuilder.attribute(2);
		bufferBuilder.attribute(2);
		this.buffer = bufferBuilder.build();

//		Font font = new Font("8x8font.png", 8, 8);
//
		Sprite sprite = Sprite.IDENTITY;

		try( Image image = Image.of("grid.png", Image.Format.RGBA) ) {
			this.texture = image.asTexture(false);
			this.texture.upload();
		}

		// Set the clear color
		glClearColor(0.01f, 0.66f, 0.92f, 0.00f);

//		font.draw("Hello!", buffer, 0, 0, 0.1f);


		World world = new World(8, 8);
		world.draw(this.buffer);
		this.buffer.bind();

		// Run the rendering loop until the user has attempted to close
		// the window or has pressed the ESCAPE key.
		while ( !window.shouldClose() ) {
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

//			float f = 300f * window.width() / window.height();

			Input input = window.input();
			scale.putFloat(input.scaleX).putFloat(input.scaleY).flush();

			this.buffer.clear();
			world.draw(this.buffer);
			this.buffer.bind();
			glDrawArrays(this.buffer.primitive, 0, this.buffer.count());

			texture.bind();
			Renderer.quad(buffer, -0.5f, -1f, 1f, 0.2f, sprite);
			this.buffer.bind();
			glDrawArrays(this.buffer.primitive, 0, this.buffer.count());

			window.swap();
		}
	}

	public static void main(String[] args) {
		Logger.info("Current working directory: ", Resources.path());
		new Main().run();
	}

}

