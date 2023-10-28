package net.darktree.warzone.client.render.color;

public final class MutableColor extends Color {

	private static final MutableColor LOCAL = new MutableColor(0, 0, 0, 0);
	private float r, g, b, a;

	public static MutableColor of(float r, float g, float b, float a) {
		return LOCAL.with(r, g, b, a);
	}

	public static MutableColor of(float r, float g, float b) {
		return LOCAL.with(r, g, b, 1);
	}

	private MutableColor(float r, float g, float b, float a) {
		with(r, g, b, a);
	}

	public MutableColor with(float r, float g, float b, float a) {
		return red(r).green(g).blue(b).alpha(a);
	}

	public MutableColor with(float r, float g, float b) {
		return with(r, g, b, a);
	}

	public MutableColor red(float r) {
		this.r = r;
		return this;
	}

	public MutableColor green(float g) {
		this.g = g;
		return this;
	}

	public MutableColor blue(float b) {
		this.b = b;
		return this;
	}

	public MutableColor alpha(float a) {
		this.a = a;
		return this;
	}

	/**
	 * Returns a non-shared copy of the original color,
	 * the returned value is safe to cache, but if you don't intend
	 * on modify the color after calling this method consider using {@link Color#immutable()} instead.
	 */
	public MutableColor unshare() {
		return new MutableColor(r, g, b, a);
	}

	@Override
	public MutableColor mutable() {
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
