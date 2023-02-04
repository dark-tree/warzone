package net.darktree.warzone.client.sound;

import net.darktree.warzone.client.sound.buffer.AudioBuffer;
import net.darktree.warzone.client.sound.buffer.NativeAudioBuffer;
import net.darktree.warzone.client.sound.source.NativeAudioSource;
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
	private static final List<NativeAudioSource> sources = new ArrayList<>();
	private static final List<NativeAudioBuffer> buffers = new ArrayList<>();
	private static boolean ready = false;
	private static final MusicPlayer music = new MusicPlayer(4, 0.25f);

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
		ALCCapabilities capabilities = ALC.createCapabilities(device);
		AL.createCapabilities(capabilities);

		ready = true;
		Logger.info("Sound system started!");
	}

	/**
	 * Shutdown the sound system
	 */
	public static void disable() {
		Logger.info("Shutting down the sound system...");
		ready = false;

		sources.forEach(NativeAudioSource::close);
		sources.clear();

		buffers.forEach(NativeAudioBuffer::close);
		buffers.clear();

		ALC10.alcMakeContextCurrent(0);
		ALC10.alcDestroyContext(context);
		ALC10.alcCloseDevice(device);
	}

	/**
	 * Checks if the Sound System is ready to play sounds
	 */
	public static boolean isReady() {
		return ready;
	}

	/**
	 * Updated attenuated sounds and cleanup the sounds that finished playing
	 */
	public static void tick() {
		sources.removeIf(source -> !source.tick());
		music.tick();
	}

	/**
	 * Load a new audio buffer from file
	 */
	public static NativeAudioBuffer createBuffer(String path) {
		NativeAudioBuffer buffer = new NativeAudioBuffer(path);
		buffers.add(buffer);

		return buffer;
	}

	/**
	 * Get a new audio source of a particular buffer
	 */
	public static NativeAudioSource createSource(NativeAudioBuffer buffer) {
		NativeAudioSource source = new NativeAudioSource(buffer);

		sources.add(source);

		return source;
	}

	/**
	 * Play a buffer as music
	 */
	public static void startMusic(AudioBuffer buffer) {
		music.play(buffer);
	}

	/**
	 * Stop all music
	 */
	public static void stopMusic() {
		music.stopAll();
	}

	/**
	 * Stop all sounds
	 */
	public static void stopAll() {
		sources.forEach(NativeAudioSource::stop);
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
