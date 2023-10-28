package net.darktree.warzone.client;

import net.darktree.warzone.client.render.shader.Program;

public class Shaders {
	public static final Program WORLD = Program.from("world");
	public static final Program TINTED = Program.from("tinted");
	public static final Program MIXED = Program.from("mixed");
	public static final Program TEXT = Program.from("text");
	public static final Program COLOR = Program.from("color");
}
