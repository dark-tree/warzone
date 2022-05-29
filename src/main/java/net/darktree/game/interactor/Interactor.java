package net.darktree.game.interactor;

import net.darktree.core.client.render.vertex.VertexBuffer;
import org.lwjgl.glfw.GLFW;

public class Interactor {

	protected boolean closed = false;

	public void draw(VertexBuffer buffer) {

	}

	public void onKey(int key, int action, int mods) {
		if(action == GLFW.GLFW_PRESS && key == GLFW.GLFW_KEY_ESCAPE) {
			closed = true;
		}
	}

	public void onClick(int button, int action, int mods, int x, int y) {

	}

	public void close() {

	}

	public boolean isClosed() {
		return closed;
	}

}
