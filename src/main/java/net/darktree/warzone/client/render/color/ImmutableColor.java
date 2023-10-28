package net.darktree.warzone.client.render.color;

public final class ImmutableColor extends Color {

	private final float r, g, b, a;

	public static ImmutableColor of(float r, float g, float b, float a) {
		return new ImmutableColor(r, g, b, a);
	}

	public static ImmutableColor of(float r, float g, float b) {
		return new ImmutableColor(r, g, b, 1);
	}

	private ImmutableColor(float r, float g, float b, float a) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}

	@Override
	public ImmutableColor immutable() {
		return this;
	}

	@Override
	public float r() {
		return r;
	}

	@Override
	public float g() {
		return g;
	}

	@Override
	public float b() {
		return b;
	}

	@Override
	public float a() {
		return a;
	}

}
