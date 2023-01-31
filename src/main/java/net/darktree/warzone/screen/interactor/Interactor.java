package net.darktree.warzone.screen.interactor;

import net.darktree.warzone.client.render.vertex.VertexBuffer;
import net.darktree.warzone.client.window.input.ClickEvent;
import net.darktree.warzone.client.window.input.KeyEvent;

public class Interactor {

	protected boolean closed = false;

	public void draw(VertexBuffer texture, VertexBuffer color) {

	}

	public void onKey(KeyEvent event) {
		if (event.isEscape()) closed = true;
	}

	public void onClick(ClickEvent event, int x, int y) {

	}

	public void close() {

	}

	public boolean isClosed() {
		return closed;
	}

}
