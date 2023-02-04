package net.darktree.warzone.client.sound.source;

import net.darktree.warzone.client.sound.buffer.AudioBuffer;

final class DummyAudioSource implements AudioSource {

	@Override
	public AudioSource setPitch(float pitch) {
		return this;
	}

	@Override
	public AudioSource setVolume(float volume) {
		return this;
	}

	@Override
	public AudioSource setLoop(boolean loop) {
		return this;
	}

	@Override
	public boolean isPlaying() {
		return false;
	}

	@Override
	public void play() {

	}

	@Override
	public void stop() {

	}

	@Override
	public boolean isOfBuffer(AudioBuffer buffer) {
		return true;
	}

}
