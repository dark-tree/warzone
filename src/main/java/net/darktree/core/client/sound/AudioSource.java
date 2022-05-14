package net.darktree.core.client.sound;

import org.lwjgl.openal.AL10;

public class AudioSource implements AutoCloseable {

	private int source;

	public AudioSource() {
		source = AL10.alGenSources();
	}

	@Override
	public void close() throws Exception {
		if (source != 0) {
			AL10.alDeleteSources(source);
			source = 0;
		}
	}

	public void setBuffer(AudioBuffer buffer) {
		AL10.alSourcei(source, AL10.AL_BUFFER, buffer.buffer);
	}

	public void setPitch(float pitch) {
		AL10.alSourcef(source, AL10.AL_PITCH, pitch);
	}

	public void setVolume(float volume) {
		AL10.alSourcef(source, AL10.AL_GAIN, volume);
	}


	public void setLoop(boolean loop) {
		AL10.alSourcei(source, AL10.AL_LOOPING, loop ? AL10.AL_TRUE : AL10.AL_FALSE);
	}

	public void setPosition(float x, float y, float z) {
		AL10.alSource3f(source, AL10.AL_POSITION, x, y, z);
	}

	public void setVelocity(float x, float y, float z) {
		AL10.alSource3f(source, AL10.AL_VELOCITY, x, y, z);
	}

	public boolean isPlaying() {
		return AL10.alGetSourcei(source, AL10.AL_SOURCE_STATE) == AL10.AL_PLAYING;
	}

	public void play() {
		AL10.alSourcePlay(source);
	}

	public void stop() {
		AL10.alSourceStop(source);
	}

}
