package net.darktree.warzone.client.gui.state;

import net.darktree.warzone.client.gui.component.UiComponent;
import net.darktree.warzone.util.Util;

import java.util.Map;
import java.util.Objects;

public class GridState {

	private final Map<Property, State> states = Util.enumMapOf(Property.class, State::new);

	public boolean get(Property property, UiComponent component) {
		return states.get(property).get(component.getIdentifier());
	}

	public void set(Property property, UiComponent component, boolean enable) {
		states.get(property).set(component.getIdentifier(), enable);
	}

	public void invalidate() {
		states.values().forEach(State::invalidate);
	}

	private final static class State {

		private final Property property;
		private UiIdentifier identifier = null;

		private State(Property property) {
			this.property = property;
		}

		private void write(UiIdentifier next) {
			if (!Objects.equals(next, identifier)) {
				if (identifier != null) identifier.component.onUpdate(property, false);
				if (next != null) next.component.onUpdate(property, true);
			}

			identifier = next;
		}

		public boolean get(UiIdentifier next) {
			return Objects.equals(identifier, next);
		}

		public void set(UiIdentifier next, boolean enable) {
			if (enable != get(next)) write(enable ? next : null);
		}

		public void invalidate() {
			write(null);
		}

	}



}
