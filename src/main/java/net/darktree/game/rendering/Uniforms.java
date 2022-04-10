package net.darktree.game.rendering;

import net.darktree.lt2d.graphics.shader.Uniform;

public class Uniforms {
	public static final Uniform SCALE = Shaders.TEXTURED.uniform("scale", Uniform.VEC2F);
	public static final Uniform OFFSET = Shaders.TEXTURED.uniform("offset", Uniform.VEC2F);
}
