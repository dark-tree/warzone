package net.darktree.core.client.sound;

import net.darktree.core.world.WorldView;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.openal.AL10;

import java.util.Objects;

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

	boolean update(@Nullable WorldView view) {
		if (attenuation && view != null) {
			updatePosition(view);
		}

		if (!isPlaying()) {
			close();
			return true;
		}

		return false;
	}

	private void updatePosition(WorldView view) {
		AL10.alSource3f(source, AL10.AL_POSITION, (view.offsetX + x) * view.scaleX, (view.offsetY + y) * view.scaleY, (z + 1 - view.zoom) * 10);
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
	public void play(@Nullable WorldView view) {
		AL10.alSourcePlay(source);

		if (attenuation) {
			updatePosition(Objects.requireNonNull(view));
		}
	}

	/**
	 * Stop playing
	 */
	public void stop() {
		AL10.alSourceStop(source);
	}

}
