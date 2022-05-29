package net.darktree.core.client.sound;

import net.darktree.core.util.Logger;
import net.darktree.core.world.WorldHolder;
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
	private static Thread thread;

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

		// FIXME
		// redesign this so that this thread either doesn't exist or only handles source deletion
		// having to pass world to sound's play() function is also dumb as this handles it already
		// why not wait for the next position update before starting to play?
		// for now the WorldHolder class has to be loaded in the main thread first to not crash

		thread = new Thread(() -> {
			while (true) {
				synchronized (sources) {
					sources.removeIf(source -> source.update(WorldHolder.world.getView()));
				}

				try {
					//noinspection BusyWait
					Thread.sleep(100);
				} catch (InterruptedException e) {
					return;
				}
			}
		}, "AudioManagerThread");
		thread.start();

		Logger.info("Sound system started!");
	}

	/**
	 * Shutdown the sound system
	 */
	public static void disable() {
		Logger.info("Shutting down the sound system...");
		thread.interrupt();

		sources.forEach(AudioSource::close);
		sources.clear();

		buffers.forEach(AudioBuffer::close);
		buffers.clear();

		ALC10.alcMakeContextCurrent(0);
		ALC10.alcDestroyContext(context);
		ALC10.alcCloseDevice(device);
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
		
		synchronized (sources) {
			sources.add(source);
		}

		return source;
	}

	/**
	 * Stop all sounds
	 */
	public static void stopAll() {
		synchronized (sources) {
			sources.forEach(AudioSource::stop);
		}
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
