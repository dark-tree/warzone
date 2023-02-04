package net.darktree.warzone.client.sound.source;

import net.darktree.warzone.Main;
import net.darktree.warzone.client.sound.buffer.AudioBuffer;
import net.darktree.warzone.util.math.MathHelper;

public final class SmoothSource {

	private final AudioSource source;
	private final float speed;

	private float rumpTime = 0;
	private double lastTime;
	private float volume;
	private boolean active = false;

	public SmoothSource(AudioSource source, float speed, float volume) {
		this.source = source;
		this.volume = volume;
		this.speed = speed;
		this.lastTime = Main.window.getRunTime();
	}

	public SmoothSource setVolume(float volume) {
		this.volume = volume;
		return this;
	}

	/**
	 * Controls the smooth rump-up of the sound effect,
	 * with 0% volume at strength=0, and ~99% at strength=1
	 */
	private void updateVolume(float strength) {
		float scale = MathHelper.sigmoid(strength * 10 - 5);
		source.setVolume(scale * volume);
	}

	public void tick() {
		double now = Main.window.getRunTime();
		float delta = (float) (now - this.lastTime);
		this.rumpTime = MathHelper.clamp(this.rumpTime + (active ? +delta : -delta), 0, speed);
		this.lastTime = now;

		updateVolume(this.rumpTime / speed);
	}

	public void stop() {
		this.active = false;
	}

	public void start() {
		this.active = true;
	}

	public boolean isOfBuffer(AudioBuffer buffer) {
		return source.isOfBuffer(buffer);
	}
	
}
