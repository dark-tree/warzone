package net.darktree.core.client.sound;

import net.darktree.core.util.Logger;
import org.lwjgl.openal.*;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

/*
 * FIXME
 * Figure out what "[ALSOFT] (EE) Failed to set real-time priority for thread: Operation not permitted (1)"
 * mens and how to fix it
 */

/**
 * Sound system implementation based on those resources: <br>
 *
 * <a href="https://ffainelli.github.io/openal-example">OpenAL Example</a> <br>
 * <a href="https://www.openal.org/documentation/OpenAL_Programmers_Guide.pdf">OpenAL Programmer's Guide</a>
 */
public class AudioManager {

	private static long device, context;

	public static void enable() {
		device = ALC10.alcOpenDevice((ByteBuffer) null);
		if (device == 0) {
			Logger.error("Failed to open OpenAL device!");
			return;
		}

		context = ALC10.alcCreateContext(device, (IntBuffer) null);
		if (context == 0) {
			Logger.error("Failed to create OpenAL context!");
		}

		ALC10.alcMakeContextCurrent(context);
		ALCCapabilities deviceCaps = ALC.createCapabilities(device);
		AL.createCapabilities(deviceCaps);
	}

	public static void disable() {
		ALC10.alcMakeContextCurrent(0);
		ALC10.alcDestroyContext(context);
		ALC10.alcCloseDevice(device);
	}

}
