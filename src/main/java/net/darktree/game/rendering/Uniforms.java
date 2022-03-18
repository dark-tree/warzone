package net.darktree.game.rendering;

import net.darktree.opengl.shader.Uniform;

public class Uniforms {
	public static final Uniform SCALE = Shaders.TEXTURED.uniform("scale", Uniform.VEC2F);
}
