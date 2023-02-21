package net.darktree.warzone.util.math;

import net.darktree.warzone.client.window.Input;
import net.darktree.warzone.client.window.Window;

public class Parallax {

	private final Input input;
	private	final Window window;
	private float px, py;
	private final float speed;

	public Parallax(Window window, float speed) {
		this.input = window.input();
		this.window = window;
		this.px = getRawX();
		this.py = getRawY();
		this.speed = speed;
	}

	/**
	 * Update the parallax values based on current mouse position
	 */
	public void update() {
		px = MathHelper.lerp(px, getRawX(), speed);
		py = MathHelper.lerp(py, getRawY(), speed);
	}

	/**
	 * Get parallax x-axis offset, returns a value between 0 and 1
	 */
	public float getX() {
		return 1 - px;
	}

	/**
	 * Get parallax y-axis offset, returns a value between 0 and 1
	 */
	public float getY() {
		return 1 - py;
	}

	private float getRawX() {
		return input.prevX / window.width();
	}

	private float getRawY() {
		return (window.height() - input.prevY) / window.height();
	}

}
