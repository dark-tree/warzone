package net.darktree.warzone.client.render;

import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL32.*;

public class GLManager {

	private static int program = 0;
	private static int texture = 0;

	/**
	 * Binds the given shader
	 */
	public static void useShader(int id) {
		if (program != id) {
			program = id;
			glUseProgram(id);
		}
	}

	/**
	 * Binds the given texture
	 */
	public static void useTexture(int id) {
		if (texture != id) {
			texture = id;
			glBindTexture(GL_TEXTURE_2D, id);
		}
	}

	/**
	 * Enables/disables blending
	 */
	public static void useBlend(boolean blend) {
		if (blend) {
			glEnable(GL_BLEND);
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		} else {
			glDisable(GL_BLEND);
		}
	}

	/**
	 * Enables/disables depth test
	 */
	public static void useDepth(boolean depth) {
		if (depth) {
			glEnable(GL_DEPTH_TEST);
			glDepthFunc(GL_LEQUAL);
		} else {
			glDisable(GL_DEPTH_TEST);
		}
	}

	/**
	 * Sets the color used when clearing the screen
	 */
	public static void useColor(float r, float g, float b) {
		glClearColor(r, g, b, 1.0f);
	}

}
