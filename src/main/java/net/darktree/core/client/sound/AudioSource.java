package net.darktree.core.client.sound;

import net.darktree.Main;
import net.darktree.core.client.window.Input;
import org.lwjgl.openal.AL10;

public class AudioSource {

	private boolean attenuation;
	private float x, y, z;
	private int source;

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

	boolean update(Input input) {
		if (attenuation) {
			updatePosition(input);
		}

		if (!isPlaying()) {
			close();
			return true;
		}

		return false;
	}

	private void updatePosition(Input input) {
		float oz = 1 - input.zoom;
		AL10.alSource3f(source, AL10.AL_POSITION, (input.offsetX + x) * input.scaleX, (input.offsetY + y) * input.scaleY, (z + oz) * 10);
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
	public void setPitch(float pitch) {
		AL10.alSourcef(source, AL10.AL_PITCH, pitch);
	}

	/**
	 * Set the volume of this source
	 */
	public void setVolume(float volume) {
		AL10.alSourcef(source, AL10.AL_GAIN, volume);
	}

	/**
	 * Enable or disable looping for this source
	 */
	public void setLoop(boolean loop) {
		AL10.alSourcei(source, AL10.AL_LOOPING, loop ? AL10.AL_TRUE : AL10.AL_FALSE);
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

		if (attenuation) {
			updatePosition(Main.window.input());
		}
	}

	/**
	 * Stop playing
	 */
	public void stop() {
		AL10.alSourceStop(source);
	}

}
