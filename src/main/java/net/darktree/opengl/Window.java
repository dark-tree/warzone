package net.darktree.opengl;

import net.darktree.util.Logger;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.APIUtil;

import java.util.Map;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL32.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window implements AutoCloseable {

	private static final Map<Integer, String> ERROR_CODES = APIUtil.apiClassTokens((field, value) -> 0x10000 < value && value < 0x20000, null, org.lwjgl.glfw.GLFW.class);
	private final Input input;
	private final int width, height;

	public final long handle;
	public static Window INSTANCE = null;

	/**
	 * Initialize GLFW, OpenGL etc. and open a window
	 */
	public static Window init(int width, int height, String title) {
		if (INSTANCE != null) {
			Logger.error("Unable to reinitialize the window!");
		}else{
			INSTANCE = new Window(width, height, title);
		}

		return INSTANCE;
	}

	private Window(int width, int height, String title) {
		glfwSetErrorCallback((error, description) -> {
			Logger.error("GLFW error occurred: ", ERROR_CODES.get(error), ", ", GLFWErrorCallback.getDescription(description));
		});

		if (!glfwInit()) {
			throw new IllegalStateException("Unable to initialize GLFW");
		}

		// window creation hints
		glfwWindowHint(GLFW_OPENGL_DEBUG_CONTEXT, GLFW_TRUE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

		//create the window
		handle = glfwCreateWindow(width, height, title, NULL, NULL);
		if ( handle == NULL ) {
			throw new RuntimeException("Failed to create the GLFW window");
		}

//		glfwSetInputMode(handle, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
		glfwSetInputMode(handle, GLFW_STICKY_KEYS, GLFW_TRUE);

		// setup event handles
		input = new Input(this);
		glfwSetKeyCallback(handle, input::keyHandle);
		glfwSetCursorPosCallback(handle, input::cursorHandle);
		glfwSetScrollCallback(handle, input::scrollHandle);

		// try centering the window on screen
		GLFWVidMode mode = glfwGetVideoMode(glfwGetPrimaryMonitor());

		if (mode != null) {
			glfwSetWindowPos(handle, (mode.width() - width) / 2, (mode.height() - height) / 2);
		} else {
			Logger.warn("Failed to acquired video mode!");
		}

		glfwMakeContextCurrent(handle);
		glfwSwapInterval(1);
		glfwShowWindow(handle);

		// load OpenGL
		GL.createCapabilities();

		// enable some basic opengl functions
		glEnable(GL_BLEND);
		glEnable(GL_CULL_FACE);
		glEnable(GL_DEPTH_TEST);

		glfwSetWindowSizeCallback(handle, (window, w, h) -> {
			glViewport(0, 0, w, h);
		});

		this.width = width;
		this.height = height;
	}


	@Override
	public void close() {
		glfwFreeCallbacks(this.handle);
		glfwDestroyWindow(this.handle);

		glfwTerminate();
		glfwSetErrorCallback(null).free();

		INSTANCE = null;
	}

	public int width() {
		return this.width;
	}

	public int height() {
		return this.height;
	}

	public Input input() {
		return this.input;
	}

	public boolean shouldClose() {
		return glfwWindowShouldClose(this.handle);
	}

	public void swap() {
		glfwSwapBuffers(this.handle);
	}
}
