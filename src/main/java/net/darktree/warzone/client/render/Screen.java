package net.darktree.warzone.client.render;

import net.darktree.warzone.client.Sounds;

public abstract class Screen {

	private boolean closed;

	public Screen() {
		closed = false;
	}

	protected boolean box(int ox, int oy, int w, int h) {
		ScreenRenderer.setOffset(ox, oy);
		return ScreenRenderer.box(w, h);
	}

	protected void text(int ox, int oy, String text, Alignment alignment) {
		ScreenRenderer.push();
		ScreenRenderer.setOffset(ox, oy);
		ScreenRenderer.setAlignment(alignment);
		ScreenRenderer.text(text, 30);
		ScreenRenderer.pop();
	}

	public abstract void draw(boolean focused);

	public void onKey(int key, int action, int mods) {

	}

	public void onClick(int button, int action, int mods) {

	}

	public void onScroll(float value) {

	}

	public void onMove(float x, float y) {

	}

	public void onResize(int w, int h) {

	}

	public void close() {
		this.closed = true;
		Sounds.PEN_CLOSE.play();
	}

	public boolean isClosed() {
		return this.closed;
	}

}
