package net.darktree.warzone.client.gui.state;

import net.darktree.warzone.client.gui.component.UiComponent;

public class UiIdentifier {

	public final UiComponent component;
	public final int x;
	public final int y;

	public UiIdentifier(UiComponent component, int x, int y) {
		this.component = component;
		this.x = x;
		this.y = y;
	}

	@Override
	public final boolean equals(Object that) {
		return that instanceof UiIdentifier id && equal(id);
	}

	private boolean equal(UiIdentifier that) {
		return same(that) || that.x == x && that.y == y;
	}

	private boolean same(UiIdentifier that) {
		return that == this || that.component == component;
	}

}
