package net.darktree.warzone.client.sound;

import net.darktree.warzone.client.sound.source.AudioSource;
import net.darktree.warzone.world.entity.Entity;
import net.darktree.warzone.world.tile.TilePos;

public interface Playable {

	AudioSource play();
	AudioSource play(int x, int y);

	default AudioSource play(Entity entity) {
		return play(entity.getX(), entity.getX());
	}

	default AudioSource play(TilePos pos) {
		return play(pos.x, pos.y);
	}

}
