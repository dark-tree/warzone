package net.darktree.warzone.world.pattern;

public class Patterns {

	public static final Pattern EMPTY = Pattern.create().build();
	public static final Pattern IDENTITY = Pattern.create().add(0, 0).build();
	public static final Pattern NEIGHBOURS = Pattern.create().add(-1, 0).add(0, -1).add(1, 0).add(0, 1).build();
	public static final Pattern CORNERS = Pattern.create().add(-1, -1).add(-1, 1).add(1, 1).add(1, -1).build();
	public static final Pattern CROSS = Pattern.create().add(NEIGHBOURS).add(IDENTITY).build();
	public static final Pattern HALO = Pattern.create().add(CORNERS).add(2, 0).add(-2, 0).add(0, 2).add(0, -2).build();
	public static final Pattern RING = Pattern.create().add(NEIGHBOURS).add(CORNERS).build();

}
