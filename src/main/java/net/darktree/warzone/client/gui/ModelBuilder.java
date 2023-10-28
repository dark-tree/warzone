package net.darktree.warzone.client.gui;

import net.darktree.warzone.client.gui.component.UiComponent;

public interface ModelBuilder {

	ModelBuilder add(int x, int y);
	<C extends UiComponent> ModelBuilder add(int x, int y, ComponentBuilder<C> builder);
	<C extends UiComponent> ModelBuilder then(Chain chain, ComponentBuilder<C> builder);

	/**
	 * This should never be called manually, instead pass the
	 * instance of {@link ModelBuilder} into {@link GridContext#setModel}
	 */
	UiComponent build(GridContext context, int x, int y);

}
