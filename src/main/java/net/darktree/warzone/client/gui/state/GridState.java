package net.darktree.warzone.client.gui.state;

import net.darktree.warzone.client.gui.component.UiComponent;
import net.darktree.warzone.util.Util;

import java.util.Map;

public class GridState {

	private final Map<Property, State> states = Util.enumMapOf(Property.class, State::new);

	public boolean get(Property property, UiComponent component) {
		return states.get(property).get(component);
	}

	public void set(Property property, UiComponent component, boolean enable) {
		states.get(property).set(component, enable);
	}

	private final static class State {

		private final Property property;
		private UiComponent component = null;

		private State(Property property) {
			this.property = property;
		}

		private void write(UiComponent next) {
			if (component != next) {
				if (component != null) component.change(property, false);
				if (next != null) next.change(property, true);
			}

			component = next;
		}

		public boolean get(UiComponent next) {
			return this.component == next;
		}

		public void set(UiComponent next, boolean enable) {
			if (enable != get(next)) write(enable ? next : null);
		}

	}



}
