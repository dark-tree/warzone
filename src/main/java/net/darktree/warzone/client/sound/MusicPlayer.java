package net.darktree.warzone.client.sound;

import net.darktree.warzone.client.sound.buffer.AudioBuffer;
import net.darktree.warzone.client.sound.source.SmoothSource;

import java.util.IdentityHashMap;
import java.util.Map;

public class MusicPlayer {

	private final Map<AudioBuffer, SmoothSource> tracks = new IdentityHashMap<>();
	private final float time, volume;
	private SmoothSource active = null;

	public MusicPlayer(float time, float volume) {
		this.time = time;
		this.volume = volume;
	}

	/**
	 * Starts playing the given track if not already playing
	 * and stops any previously playing track
	 */
	public void play(AudioBuffer track) {
		if (active != null) {
			if (active.isOfBuffer(track)) {
				return;
			}

			active.stop();
		}

		active = track.play().setLoop(true).getInterpolated(time, volume);
		tracks.put(track, active);
		active.start();
	}

	/**
	 * Updates all tracks
	 */
	public void tick() {
		tracks.values().forEach(SmoothSource::tick);
	}

	/**
	 * Stops all tracks
	 */
	public void stopAll() {
		tracks.values().forEach(SmoothSource::stop);
	}

}
