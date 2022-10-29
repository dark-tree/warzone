package net.darktree.core.client;

import net.darktree.core.client.render.vertex.VertexBuffer;

public class Buffers {
	public static final VertexBuffer.Builder IMMEDIATE = VertexBuffer.create().attribute("xy").attribute("uv").attribute("rgba").immediate();
	public static final VertexBuffer.Builder STATIC = VertexBuffer.create().attribute("xy").attribute("uv").attribute("rgba");
}
