package net.darktree;

import net.darktree.opengl.image.Image;
import net.darktree.opengl.image.Texture;
import net.darktree.opengl.shader.Program;
import net.darktree.opengl.vertex.VertexBuffer;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL32;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Main {

	// The window handle
	private long window;

	public void run() {
		Logger.info("Using LWJGL ", Version.getVersion());

		init();
		loop();

		this.texture.close();
		this.buffer.close();
		this.program.close();

		// Free the window callbacks and destroy the window
		glfwFreeCallbacks(window);
		glfwDestroyWindow(window);

		// Terminate GLFW and free the error callback
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}

	private void init() {
		// Setup an error callback. The default implementation
		// will print the error message in System.err.
		GLFWErrorCallback.createPrint(System.err).set();

		// Initialize GLFW. Most GLFW functions will not work before doing this.
		if ( !glfwInit() )
			throw new IllegalStateException("Unable to initialize GLFW");

		// Configure GLFW
		glfwDefaultWindowHints(); // optional, the current window hints are already the default
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

		// Create the window
		window = glfwCreateWindow(300, 300, "Hello World!", NULL, NULL);
		if ( window == NULL )
			throw new RuntimeException("Failed to create the GLFW window");

		// Setup a key callback. It will be called every time a key is pressed, repeated or released.
		glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
			if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
				glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
		});

		// Get the thread stack and push a new frame
		try ( MemoryStack stack = stackPush() ) {
			IntBuffer pWidth = stack.mallocInt(1); // int*
			IntBuffer pHeight = stack.mallocInt(1); // int*

			// Get the window size passed to glfwCreateWindow
			glfwGetWindowSize(window, pWidth, pHeight);

			// Get the resolution of the primary monitor
			GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

			// Center the window
			glfwSetWindowPos(
					window,
					(vidmode.width() - pWidth.get(0)) / 2,
					(vidmode.height() - pHeight.get(0)) / 2
			);
		} // the stack frame is popped automatically

		// Make the OpenGL context current
		glfwMakeContextCurrent(window);

		// Enable v-sync
		glfwSwapInterval(1);

		// Make the window visible
		glfwShowWindow(window);

	}

	private Program program;
	private VertexBuffer buffer;
	private Texture texture;

	private void loop() {
		// This line is critical for LWJGL's interoperation with GLFW's
		// OpenGL context, or any context that is managed externally.
		// LWJGL detects the context that is current in the current thread,
		// creates the GLCapabilities instance and makes the OpenGL
		// bindings available for use.
		GL.createCapabilities();

		Program.Builder programBuilder = Program.create();
		programBuilder.add("shader/textured.frag", GL32.GL_FRAGMENT_SHADER);
		programBuilder.add("shader/textured.vert", GL32.GL_VERTEX_SHADER);
		this.program = programBuilder.link();
		this.program.bind();

		VertexBuffer.Builder bufferBuilder = VertexBuffer.create();
		bufferBuilder.attribute(2);
		bufferBuilder.attribute(2);
		this.buffer = bufferBuilder.build();

		try( Image image = Image.of("test.png", Image.Format.RGBA) ) {
			this.texture = image.asTexture(false);
			this.texture.upload();
		}

		// Set the clear color
		glClearColor(1.0f, 0.0f, 0.0f, 0.0f);

		this.buffer.putFloat(0).putFloat(0).putFloat(0).putFloat(0);
		this.buffer.putFloat(1).putFloat(0).putFloat(1).putFloat(0);
		this.buffer.putFloat(0).putFloat(1).putFloat(0).putFloat(1);

		this.buffer.putFloat(1).putFloat(0).putFloat(1).putFloat(0);
		this.buffer.putFloat(1).putFloat(1).putFloat(1).putFloat(1);
		this.buffer.putFloat(0).putFloat(1).putFloat(0).putFloat(1);

		this.buffer.bind();

		// Run the rendering loop until the user has attempted to close
		// the window or has pressed the ESCAPE key.
		while ( !glfwWindowShouldClose(window) ) {
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

			render();

			glfwSwapBuffers(window); // swap the color buffers

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

