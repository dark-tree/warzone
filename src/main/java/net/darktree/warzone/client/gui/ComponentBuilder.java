package net.darktree.warzone.client.gui;

import net.darktree.warzone.client.gui.component.UiComponent;

public interface ComponentBuilder <C extends UiComponent> {

	int getWidth();
	int getHeight();
	C build(GridContext context, int x, int y);

}
