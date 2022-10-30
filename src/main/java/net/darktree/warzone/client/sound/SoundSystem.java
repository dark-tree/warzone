package net.darktree.warzone.client.sound;

import net.darktree.warzone.util.Logger;
import org.lwjgl.openal.*;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Sound system implementation based on those resources: <br>
 *
 * <a href="https://ffainelli.github.io/openal-example">OpenAL Example</a> <br>
 * <a href="https://www.openal.org/documentation/OpenAL_Programmers_Guide.pdf">OpenAL Programmer's Guide</a>
 */
public class SoundSystem {

	private static long device, context;
	private static final List<AudioSource> sources = new ArrayList<>();
	private static final List<AudioBuffer> buffers = new ArrayList<>();

	/**
	 * Initialize and enable the sound system
	 */
	public static void enable() {
		device = ALC10.alcOpenDevice((ByteBuffer) null);
		if (device == 0) {
			Logger.error("Sound system failed to start, unable to open device!");
			return;
		}

		context = ALC10.alcCreateContext(device, (IntBuffer) null);
		if (context == 0) {
			Logger.error("Sound system failed to start, unable to create context!");
			return;
		}

		ALC10.alcMakeContextCurrent(context);
		ALCCapabilities deviceCaps = ALC.createCapabilities(device);
		AL.createCapabilities(deviceCaps);

		Logger.info("Sound system started!");
	}

	/**
	 * Shutdown the sound system
	 */
	public static void disable() {
		Logger.info("Shutting down the sound system...");

		sources.forEach(AudioSource::close);
		sources.clear();

		buffers.forEach(AudioBuffer::close);
		buffers.clear();

		ALC10.alcMakeContextCurrent(0);
		ALC10.alcDestroyContext(context);
		ALC10.alcCloseDevice(device);
	}

	/**
	 * Updated attenuated sounds and cleanup the sounds that finished playing
	 */
	public static void tick() {
		sources.removeIf(source -> !source.tick());
	}

	/**
	 * Load a new audio buffer from file
	 */
	public static AudioBuffer createBuffer(String path) {
		AudioBuffer buffer = new AudioBuffer(path);
		buffers.add(buffer);

		return buffer;
	}

	/**
	 * Get a new audio source of a particular buffer
	 */
	public static AudioSource createSource(AudioBuffer buffer) {
		AudioSource source = new AudioSource();
		source.setBuffer(buffer);

		sources.add(source);

		return source;
	}

	/**
	 * Stop all sounds
	 */
	public static void stopAll() {
		sources.forEach(AudioSource::stop);
	}

	/**
	 * Get the number of current sound sources
	 */
	public static int getSourceCount() {
		return sources.size();
	}

	/**
	 * Set the master volume
	 */
	public static void setMasterVolume(float volume) {
		AL10.alListenerf(AL10.AL_GAIN, volume);
	}

}
