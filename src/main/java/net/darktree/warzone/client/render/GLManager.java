package net.darktree.warzone.client.render;

import static org.lwjgl.opengl.GL32.*;

public class GLManager {

	private static int program = 0;
	private static int texture = 0;

	public static void useShader(int id) {
		if (program != id) {
			program = id;
			glUseProgram(id);
		}
	}

	public static void useTexture(int id) {
		if (texture != id) {
			texture = id;
			glBindTexture(GL_TEXTURE_2D, id);
		}
	}

	public static void useBlend(boolean blend) {
		if (blend) {
			glEnable(GL_BLEND);
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		} else {
			glDisable(GL_BLEND);
		}
	}

	public static void useDepth(boolean depth) {
		if (depth) {
			glEnable(GL_DEPTH_TEST);
			glDepthFunc(GL_LEQUAL);
		} else {
			glDisable(GL_DEPTH_TEST);
		}
	}

}
