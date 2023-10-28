package net.darktree.warzone.client.render.color;

import net.darktree.warzone.util.math.MathHelper;

public abstract class Color {

	/**
	 * Returns a shared (non-persistent) mutable view of this color,
	 * the returned value is only safe for rendering and CAN'T be cached,
	 * to make it persistent call {@link MutableColor#unshare()} or {@link Color#immutable()} on it
	 */
	public MutableColor mutable() {
		return MutableColor.of(r(), g(), b(), a());
	}

	/**
	 * Returns a new immutable view of this color
	 */
	public ImmutableColor immutable() {
		return ImmutableColor.of(r(), g(), b(), a());
	}

	public abstract float r();
	public abstract float g();
	public abstract float b();
	public abstract float a();

	@Override
	public String toString() {
		return "Color{" + r() + ", " + g() + ", " + b() + ", " + a() + '}';
	}

	/**
	 * Lerps between two given colors, the calculation is done in RGB space.
	 * This function returns a MutableColor view, to make it persistent call {@link MutableColor#unshare()} or {@link Color#immutable()} on it.
	 *
	 * @param delta value between 0.0f and 1.0f
 	 */
	public static MutableColor lerp(Color a, Color b, float delta) {
		return MutableColor.of(MathHelper.lerp(a.r(), b.r(), delta), MathHelper.lerp(a.g(), b.g(), delta), MathHelper.lerp(a.b(), b.b(), delta), MathHelper.lerp(a.a(), b.a(), delta));
	}

}
