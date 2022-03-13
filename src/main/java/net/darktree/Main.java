package net.darktree;

import net.darktree.game.World;
import net.darktree.opengl.Window;
import net.darktree.opengl.shader.Program;
import net.darktree.opengl.vertex.VertexBuffer;
import net.darktree.util.Logger;
import net.darktree.util.Resources;
import org.lwjgl.Version;
import org.lwjgl.opengl.GL32;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Main {

	Window window;

	public static float scale = 0.1f, x=0, y=0;

	public double ox, oy;

	public void run() {
		Logger.info("Using LWJGL ", Version.getVersion());

		window = Window.init(300, 300, "Hello!");
		loop();

//		this.texture.close();
		this.buffer.close();
		this.program.close();

		window.close();
	}

	private Program program;
	private VertexBuffer buffer;
//	private Texture texture;

	private void loop() {

		Program.Builder programBuilder = Program.create();
		programBuilder.add("shader/textured.frag", GL32.GL_FRAGMENT_SHADER);
		programBuilder.add("shader/textured.vert", GL32.GL_VERTEX_SHADER);
		this.program = programBuilder.link();
		this.program.bind();

		VertexBuffer.Builder bufferBuilder = VertexBuffer.create();
		bufferBuilder.attribute(2);
		bufferBuilder.attribute(2);
		this.buffer = bufferBuilder.build();

//		Font font = new Font("8x8font.png", 8, 8);
//
//		try( Image image = Image.of("test.png", Image.Format.RGBA) ) {
//			this.texture = image.asTexture(false);
//			this.texture.upload();
//		}

		// Set the clear color
		glClearColor(1.0f, 0.0f, 0.0f, 0.0f);

//		font.draw("Hello!", buffer, 0, 0, 0.1f);

		World world = new World(8, 8);
		world.draw(this.buffer);
		this.buffer.bind();

		// Run the rendering loop until the user has attempted to close
		// the window or has pressed the ESCAPE key.
		while ( !window.shouldClose() ) {
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

			this.buffer.clear();
			world.draw(this.buffer);
			this.buffer.bind();

			glDrawArrays(this.buffer.primitive, 0, this.buffer.count());

			window.swap();

			// Poll for window events. The key callback above will only be
			// invoked during this call.
			glfwPollEvents();
		}
	}

	private void render() {
//		this.buffer.bind();

		glDrawArrays(this.buffer.primitive, 0, this.buffer.count());
	}

	public static void main(String[] args) {
		Logger.info("Current working directory: ", Resources.path());
		new Main().run();
	}

}

