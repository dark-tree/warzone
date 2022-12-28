package net.darktree.warzone.client.sound.source;

public interface AudioSource {

	static AudioSource dummy() {
		return new DummyAudioSource();
	}

	/**
	 * Set the pitch of this source
	 */
	AudioSource setPitch(float pitch);

	/**
	 * Set the volume of this source
	 */
	AudioSource setVolume(float volume);

	/**
	 * Enable or disable looping for this source
	 */
	AudioSource setLoop(boolean loop);

	/**
	 * Check if the audio is being played
	 */
	boolean isPlaying();

	/**
	 * Start playing
	 */
	void play();

	/**
	 * Stop playing
	 */
	void stop();

}
