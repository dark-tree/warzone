package net.darktree.warzone.client.sound;

import net.darktree.warzone.world.WorldHolder;
import net.darktree.warzone.world.WorldView;
import org.lwjgl.openal.AL10;

public class AudioSource {

	private boolean attenuation;
	private float x, y, z;
	private int source;
	private boolean playing;

	AudioSource() {
		source = AL10.alGenSources();
		attenuation = false;

		// the distance at which only half of the volume is heard
		AL10.alSourcef(source, AL10.AL_REFERENCE_DISTANCE, 5);
	}

	void close() {
		if (source != 0) {
			AL10.alDeleteSources(source);
			source = 0;
		}
	}

	/**
	 * Set the audio buffer from which to play the sound
	 */
	public void setBuffer(AudioBuffer buffer) {
		AL10.alSourcei(source, AL10.AL_BUFFER, buffer.getHandle());
	}

	/**
	 * Set the pitch of this source
	 */
	public AudioSource setPitch(float pitch) {
		AL10.alSourcef(source, AL10.AL_PITCH, pitch);
		return this;
	}

	/**
	 * Set the volume of this source
	 */
	public AudioSource setVolume(float volume) {
		AL10.alSourcef(source, AL10.AL_GAIN, volume);
		return this;
	}

	/**
	 * Enable or disable looping for this source
	 */
	public AudioSource setLoop(boolean loop) {
		AL10.alSourcei(source, AL10.AL_LOOPING, loop ? AL10.AL_TRUE : AL10.AL_FALSE);
		return this;
	}

	/**
	 * Enable or disable attenuation for this source
	 */
	public void setAttenuation(boolean attenuation) {
		this.attenuation = attenuation;
	}

	/**
	 * Positions from which the audio should be played
	 */
	public void setPosition(float x, float y, float z) {
		this.x = x + 0.5f;
		this.y = y + 0.5f;
		this.z = z;
	}

	/**
	 * Check if the audio is being played
	 */
	public boolean isPlaying() {
		return AL10.alGetSourcei(source, AL10.AL_SOURCE_STATE) == AL10.AL_PLAYING;
	}

	/**
	 * Start playing
	 */
	public void play() {
		AL10.alSourcePlay(source);
		positionUpdate();
	}

	/**
	 * Stop playing
	 */
	public void stop() {
		AL10.alSourceStop(source);
	}

	boolean tick() {
		positionUpdate();

		if (!isPlaying()) {
			close();
			return false;
		}

		return true;
	}

	private void positionUpdate() {
		if (attenuation) {
			WorldView view = WorldHolder.world.getView();
			AL10.alSource3f(source, AL10.AL_POSITION, (view.offsetX + x) * view.scaleX, (view.offsetY + y) * view.scaleY, (z + 1 - view.zoom) * 10);
		}
	}

}
