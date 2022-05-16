package net.darktree.core.client.sound;

import net.darktree.core.util.Logger;
import net.darktree.core.util.Resources;
import org.lwjgl.openal.AL10;
import org.lwjgl.stb.STBVorbis;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;
import java.nio.ShortBuffer;

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

	private int formatOf(int channels, int sampleRate) {
		boolean stereo = channels > 1;

		if (stereo) {
			Logger.warn("Stereo audio detected, attenuation is not supported!");
		}

//		if (sampleRate == 16) {
//			return stereo ? AL10.AL_FORMAT_STEREO16 : AL10.AL_FORMAT_MONO16;
//		}
//
//		if (sampleRate == 8) {
//			return stereo ? AL10.AL_FORMAT_STEREO8 : AL10.AL_FORMAT_MONO8;
//		}
//
//		throw new RuntimeException("Unknown sound format!");

		// TODO figure out why STBVorbis tells me that my sound file has sampleRate=48000
		return AL10.AL_FORMAT_MONO16;
	}

	private void load(String path) {
		Logger.info("Loading sound file: ", path);

		try (MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer channelsBuffer = stack.mallocInt(1);
			IntBuffer sampleRateBuffer = stack.mallocInt(1);

			ShortBuffer data = STBVorbis.stb_vorbis_decode_filename(Resources.location(path).toString(), channelsBuffer, sampleRateBuffer);

			if (data == null) {
				Logger.warn("Failed to decode sound file: ", path);
				return;
			}

			int channels = channelsBuffer.get();
			int rate = sampleRateBuffer.get();

			try {
				AL10.alBufferData(buffer, formatOf(channels, rate), data, rate);
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
