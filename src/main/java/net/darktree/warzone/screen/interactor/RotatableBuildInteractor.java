package net.darktree.warzone.screen.interactor;

import net.darktree.warzone.client.Sounds;
import net.darktree.warzone.client.window.input.KeyEvent;
import net.darktree.warzone.world.World;
import net.darktree.warzone.world.entity.building.Building;
import org.lwjgl.glfw.GLFW;

public class RotatableBuildInteractor extends BuildInteractor {

	public RotatableBuildInteractor(Building.Type type, World world) {
		super(type, world);
	}

	@Override
	public void onKey(KeyEvent event) {
		super.onKey(event);

		if (event.isPressed(GLFW.GLFW_KEY_R)) {
			rotation = rotation.next();
			onRotated();
		}
	}

	public void onRotated() {
		Sounds.ROTATE.play(pos);
	}

}
