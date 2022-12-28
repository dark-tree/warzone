package net.darktree.warzone.client.sound.buffer;

import net.darktree.warzone.client.sound.SoundSystem;
import net.darktree.warzone.client.sound.source.AudioSource;
import net.darktree.warzone.client.sound.source.NativeAudioSource;
import net.darktree.warzone.util.Logger;
import net.darktree.warzone.util.Resources;
import org.lwjgl.openal.AL10;
import org.lwjgl.stb.STBVorbis;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.Objects;

public final class NativeAudioBuffer implements AudioBuffer {

	private int buffer;

	public NativeAudioBuffer(String path) {
		buffer = AL10.alGenBuffers();
		load(path);
	}

	public void close() {
		if (buffer != 0) {
			AL10.alDeleteBuffers(buffer);
			buffer = 0;
		}
	}

	private int formatOf(int channels) {
		boolean stereo = channels > 1;

		if (stereo) {
			Logger.warn("Stereo audio detected, attenuation is not supported!");
			return AL10.AL_FORMAT_STEREO16;
		}

		return AL10.AL_FORMAT_MONO16;
	}

	private void load(String path) {
		Logger.info("Loading sound file: ", path);

		try (MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer channelCount = stack.mallocInt(1);
			IntBuffer sampleRate = stack.mallocInt(1);

			ShortBuffer data = STBVorbis.stb_vorbis_decode_filename(Objects.requireNonNull(Resources.location(path)).toString(), channelCount, sampleRate);

			if (data == null) {
				Logger.warn("Failed to decode sound file: ", path);
				return;
			}

			try {
				AL10.alBufferData(buffer, formatOf(channelCount.get()), data, sampleRate.get());
			}catch (Exception e) {
				Logger.warn("Failed to upload sound file: ", path);
				e.printStackTrace();
			}
		}
	}

	public int getHandle() {
		return buffer;
	}

	@Override
	public AudioSource play() {
		NativeAudioSource source = SoundSystem.createSource(this);
		source.play();

		return source;
	}

	@Override
	public AudioSource play(int x, int y) {
		NativeAudioSource source = SoundSystem.createSource(this);
		source.setPosition(x, y, 0);
		source.setAttenuation(true);
		source.play();

		return source;
	}

}
