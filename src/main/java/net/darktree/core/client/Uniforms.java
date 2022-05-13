package net.darktree.core.client;

import net.darktree.core.client.render.shader.Uniform;

public class Uniforms {
	public static final Uniform SCALE = Shaders.WORLD.uniform("scale", Uniform.VEC2F);
	public static final Uniform OFFSET = Shaders.WORLD.uniform("offset", Uniform.VEC2F);
}
