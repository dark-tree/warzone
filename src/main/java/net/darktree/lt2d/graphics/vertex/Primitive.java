package net.darktree.lt2d.graphics.vertex;

import org.lwjgl.opengl.GL32;

public enum Primitive {
	TRIANGLES(GL32.GL_TRIANGLES);

	final int type;

	Primitive(int type) {
		this.type = type;
	}
}
