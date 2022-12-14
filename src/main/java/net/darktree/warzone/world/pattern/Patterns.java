package net.darktree.warzone.world.pattern;

public class Patterns {

	public static final Pattern EMPTY = FixedPattern.create().build();
	public static final Pattern IDENTITY = FixedPattern.create().add(0, 0).build();
	public static final Pattern SQUARE = FixedPattern.create().add(0, 0).add(1, 0).add(0, 1).add(1, 1).build();
	public static final Pattern SMALL_CROSS = FixedPattern.create().addManhattan(1).build();
	public static final Pattern LARGE_CROSS = FixedPattern.create().addManhattan(2).build();
	public static final Pattern STAR = FixedPattern.create().add(-1, -1).add(-1, 1).add(1, 1).add(1, -1).add(2, 0).add(-2, 0).add(0, 2).add(0, -2).build();

	public static Pattern area(int w, int h) {
		return new AreaPattern(w, h);
	}

}
