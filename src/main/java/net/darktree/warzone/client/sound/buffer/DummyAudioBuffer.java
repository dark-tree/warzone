package net.darktree.warzone.client.sound.buffer;

import net.darktree.warzone.client.sound.source.AudioSource;
import net.darktree.warzone.util.Logger;

final class DummyAudioBuffer implements AudioBuffer {

	public DummyAudioBuffer(String path) {
		Logger.warn("Using dummy buffer for sound: ", path);
	}

	@Override
	public AudioSource play() {
		return AudioSource.dummy();
	}

	@Override
	public AudioSource play(int x, int y) {
		return AudioSource.dummy();
	}

}
