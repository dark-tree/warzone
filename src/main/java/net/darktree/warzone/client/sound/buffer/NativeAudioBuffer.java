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
		return (channels > 1) ? AL10.AL_FORMAT_STEREO16 : AL10.AL_FORMAT_MONO16;
	}

	private void load(String path) {
		int format = 0;

		try (MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer channelCount = stack.mallocInt(1);
			IntBuffer sampleRate = stack.mallocInt(1);

			ShortBuffer data = STBVorbis.stb_vorbis_decode_filename(Objects.requireNonNull(Resources.location(path)).toString(), channelCount, sampleRate);

			if (data == null) {
				Logger.error("Failed to load sound: '", path, "' failed, unable to decode data!");
				return;
			}

			try {
				format = formatOf(channelCount.get());
				AL10.alBufferData(buffer, format, data, sampleRate.get());
			}catch (Exception e) {
				Logger.error("Failed to load sound: '", path, "', unable to upload data!");
				e.printStackTrace();
			}

			if (format == AL10.AL_FORMAT_STEREO16) {
				Logger.warn("Attenuation not supported for stereo sound: '", path, "'!");
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
