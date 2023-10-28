package net.darktree.warzone.client.gui;

import net.darktree.warzone.client.gui.component.UiComponent;
import net.darktree.warzone.client.gui.component.UiNull;

public final class ComponentFactory<C extends UiComponent> {

	private final ComponentBuilder<C> builder;
	private final int ox;
	private final int oy;

	private ComponentFactory(ComponentBuilder<C> builder, int ox, int oy) {
		this.builder = builder;
		this.ox = ox;
		this.oy = oy;
	}

	public static <C extends UiComponent> ComponentFactory<C> wrap(ComponentBuilder<C> builder, int x, int y) {
		return new ComponentFactory<>(builder, x, y);
	}

	public int getWidth() {
		return builder.getWidth();
	}

	public int getHeight() {
		return builder.getHeight();
	}

	public boolean nonNull() {
		return builder != UiNull.of();
	}

	public UiComponent build(GridContext context, int x, int y) {
		return builder.build(context, x + ox, y + oy);
	}

}
