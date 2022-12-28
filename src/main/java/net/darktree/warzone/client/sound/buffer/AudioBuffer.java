package net.darktree.warzone.client.sound.buffer;

import net.darktree.warzone.client.sound.Playable;
import net.darktree.warzone.client.sound.SoundSystem;

public interface AudioBuffer extends Playable {

	static AudioBuffer of(String path) {
		return SoundSystem.isReady() ? SoundSystem.createBuffer(path) : new DummyAudioBuffer(path);
	}

}
