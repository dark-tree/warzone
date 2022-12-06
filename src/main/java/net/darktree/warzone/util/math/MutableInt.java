package net.darktree.warzone.util.math;

public class MutableInt {

	public int value;

	public MutableInt(int value) {
		this.value = value;
	}

	public void addNonNegative(int value) {
		final int sum = this.value + value;

		if (sum < 0) {
			throw new ArithmeticException("Value must not be negative!");
		}

		this.value = sum;
	}

}
