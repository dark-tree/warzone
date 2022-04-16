package net.darktree.lt2d.graphics;

import net.darktree.Main;
import net.darktree.lt2d.util.Logger;
import net.darktree.lt2d.world.path.Pathfinder;
import net.darktree.lt2d.world.World;
import net.darktree.lt2d.world.entities.MovingEntity;
import net.darktree.lt2d.world.path.Path;
import net.querz.nbt.io.NBTUtil;
import net.querz.nbt.tag.CompoundTag;
import org.lwjgl.glfw.GLFW;

import java.io.IOException;

public class Input {

	private final Window window;

	private float prevX;
	private float prevY;
	private float zoomMin;
	private float zoomMax;

	// TODO make not public (maybe)
	public float offsetX;
	public float offsetY;
	public float zoom = 0.1f;
	public float scaleX;
	public float scaleY;
	public float guiScale = 1;

	public Input(Window window) {
		this.window = window;
	}

	public void setZoomRange(float min, float max) {
		this.zoomMin = min;
		this.zoomMax = max;
	}

	public void setGuiScale(float scale) {
		this.guiScale = scale;
	}

	void keyHandle(long handle, int key, int scancode, int action, int mods) {
		if(action == GLFW.GLFW_PRESS && key == GLFW.GLFW_KEY_S) {
			CompoundTag tag = new CompoundTag();
			Main.world.toNbt(tag);
			try {
				NBTUtil.write(tag, "./map.dat", true);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if(action == GLFW.GLFW_PRESS && key == GLFW.GLFW_KEY_L) {
			try {
				Main.world = World.load((CompoundTag) NBTUtil.read("./map.dat", true).getTag());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if(action == GLFW.GLFW_PRESS && key == GLFW.GLFW_KEY_SPACE) {
			Pathfinder pathfinder = new Pathfinder(Main.world, 0, 0, 50);
			Logger.info("canReach: ", pathfinder.canReach(5, 5));
			Path path = pathfinder.getPathTo(5, 5);

			((MovingEntity) Main.world.getEntities().get(0)).follow(path);
		}
	}

	void cursorHandle(long handle, double x, double y) {
		if (this.isButtonPressed(GLFW.GLFW_MOUSE_BUTTON_3)) {
			var ox = (prevX - x) / window.width() * -2 / scaleX;
			var oy = (prevY - y) / window.height() * 2 / scaleY;

			offsetX += ox;
			offsetY += oy;
		}

		prevX = (float) x;
		prevY = (float) y;
	}

	// time_wasted_while_trying_to_fucking_make_this_work_again = 3.5h
	void clickHandle(long handle, int button, int action, int mods) {
		float x = ((prevX / window.width() * 2 / scaleX) - offsetX) * scaleX * Main.world.width/2;
		float y = ((prevY / window.height() * 2 / scaleY) + offsetY) * scaleY * Main.world.height/2;

		if(button == GLFW.GLFW_MOUSE_BUTTON_1 || button == GLFW.GLFW_MOUSE_BUTTON_2) {
			try {
				Main.world.getTileState((int) x, Main.world.height - (int) y).getTile().onInteract(Main.world, (int) x, Main.world.height - (int) y, action);;
			}catch (IndexOutOfBoundsException ignore) {

			}
		}

		Logger.info(x, " ", y);
	}

	void updateScale() {
		scaleX = zoom * window.height() / (float) window.width();
		scaleY = zoom;
	}

	// time_wasted_while_trying_to_fucking_make_this_work_again = 2.5h
	void scrollHandle(long handle, double x, double y) {
		zoom += (float) (zoom * y * 0.15f);

		if (zoom < zoomMin) zoom = zoomMin;
		if (zoom > zoomMax) zoom = zoomMax;

		updateScale();
	}

	public boolean isKeyPressed(int key) {
		return GLFW.glfwGetKey(window.handle, key) == GLFW.GLFW_PRESS;
	}

	public boolean isButtonPressed(int button) {
		return GLFW.glfwGetMouseButton(window.handle, button) == GLFW.GLFW_PRESS;
	}
}
