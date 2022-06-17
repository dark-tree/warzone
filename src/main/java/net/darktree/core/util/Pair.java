package net.darktree.core.util;

import java.util.Objects;

public final class Pair<A, B> {

	public final A a;
	public final B b;

	public Pair(A a, B b) {
		this.a = a;
		this.b = b;
	}

	/**
	 * Creates new instance of the Pair object
	 */
	public static <A, B> Pair<A, B> of(A a, B b) {
		return new Pair<>(a, b);
	}

	/**
	 * Returns a pair with swapped values
	 */
	public Pair<B, A> swap() {
		return Pair.of(b, a);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;

		if (obj instanceof Pair other) {
			return Objects.equals(a, other.a) && Objects.equals(b, other.b);
		}

		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(a, b);
	}

	@Override
	public String toString() {
		return "Pair{a=" + a + ", b=" + b + "}";
	}

}
