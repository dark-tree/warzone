package net.darktree.core.client.sound;

import net.darktree.core.util.Logger;
import net.darktree.core.util.Resources;
import org.lwjgl.openal.AL10;
import org.lwjgl.stb.STBVorbis;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.Objects;

public class AudioBuffer {

	private int buffer;

	AudioBuffer(String path) {
		buffer = AL10.alGenBuffers();
		load(path);
	}

	void close() {
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

}
