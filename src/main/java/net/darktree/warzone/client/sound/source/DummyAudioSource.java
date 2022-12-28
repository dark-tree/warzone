package net.darktree.warzone.client.sound.source;

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

}
