package net.darktree.warzone.client.sound.source;

import net.darktree.warzone.Main;
import net.darktree.warzone.client.sound.buffer.NativeAudioBuffer;
import net.darktree.warzone.world.WorldView;
import org.lwjgl.openal.AL10;

public final class NativeAudioSource implements AudioSource {

	private boolean attenuation;
	private float x, y, z;
	private int source;

	public NativeAudioSource(NativeAudioBuffer buffer) {
		source = AL10.alGenSources();
		attenuation = false;

		// the distance at which only half of the volume is heard
		AL10.alSourcef(source, AL10.AL_REFERENCE_DISTANCE, 5);
		AL10.alSourcei(source, AL10.AL_BUFFER, buffer.getHandle());
	}

	public void close() {
		if (source != 0) {
			AL10.alDeleteSources(source);
			source = 0;
		}
	}

	@Override
	public AudioSource setPitch(float pitch) {
		AL10.alSourcef(source, AL10.AL_PITCH, pitch);
		return this;
	}

	@Override
	public AudioSource setVolume(float volume) {
		AL10.alSourcef(source, AL10.AL_GAIN, volume);
		return this;
	}

	@Override
	public AudioSource setLoop(boolean loop) {
		AL10.alSourcei(source, AL10.AL_LOOPING, loop ? AL10.AL_TRUE : AL10.AL_FALSE);
		return this;
	}

	@Override
	public boolean isPlaying() {
		return AL10.alGetSourcei(source, AL10.AL_SOURCE_STATE) == AL10.AL_PLAYING;
	}

	@Override
	public void play() {
		AL10.alSourcePlay(source);
		positionUpdate();
	}

	@Override
	public void stop() {
		AL10.alSourceStop(source);
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

	public boolean tick() {
		positionUpdate();

		if (!isPlaying()) {
			close();
			return false;
		}

		return true;
	}

	private void positionUpdate() {
		if (attenuation) {
			Main.game.getWorld().ifPresent(world -> {
				WorldView view = world.getView();
				AL10.alSource3f(source, AL10.AL_POSITION, (view.offsetX + x) * view.scaleX, (view.offsetY + y) * view.scaleY, (z + 1 - view.zoom) * 10);
			});
		}
	}

}
