package net.darktree.warzone.client.gui.component;

import net.darktree.warzone.client.gui.*;
import net.darktree.warzone.client.render.color.Color;
import net.darktree.warzone.client.render.color.ImmutableColor;
import net.darktree.warzone.client.window.input.Event;

import java.util.ArrayList;
import java.util.List;

public class UiComposed extends UiComponent {

	private static final Color COLOR = ImmutableColor.of(0, 0, 1);
	private final List<UiComponent> components;

	protected UiComposed(GridContext context, int x, int y, int width, int height, List<UiComponent> components) {
		super(context, x, y, width, height, Inset.empty());
		this.components = components;
	}

	public static Builder of() {
		return new Builder();
	}

	@Override
	public void react(Event event, GridContext grid) {
		components.forEach(component -> component.react(event, grid));
	}

	@Override
	public void draw(DrawContext context, GridContext grid) {
		drawDebugOverlay(context, COLOR);
		components.forEach(component -> component.draw(context, grid));
	}

	static public class Builder extends UiComponent.Builder<UiComposed, Builder> implements ModelBuilder {

		private int minX = Integer.MAX_VALUE;
		private int minY = Integer.MAX_VALUE;
		private int maxX = Integer.MIN_VALUE;
		private int maxY = Integer.MIN_VALUE;

		private int x = 0;
		private int y = 0;
		private final List<ComponentFactory<?>> components = new ArrayList<>();

		private ComponentFactory<?> getLast() {
			if (components.isEmpty()) {
				throw new RuntimeException("Component chain needs to start with a unchained component!");
			}

			return components.get(components.size() - 1);
		}

		private void append(ComponentFactory<?> builder) {
			int mx = x + builder.getWidth();
			int my = y + builder.getHeight();

			// expand bounding box
			if (x < minX) minX = x;
			if (y < minY) minY = y;
			if (mx > maxX) maxX = mx;
			if (my > maxY) maxY = my;

			// add to list
			this.components.add(builder);
		}

		@Override
		public Builder add(int x, int y) {
			this.x = x;
			this.y = y;
			append(ComponentFactory.wrap(UiNull.of(0, 0), x, y));
			return this;
		}

		@Override
		public <C extends UiComponent> Builder add(int x, int y, ComponentBuilder<C> builder) {
			this.x = x;
			this.y = y;
			append(ComponentFactory.wrap(builder, x, y));
			return this;
		}

		@Override
		public <C extends UiComponent> Builder then(Chain chain, ComponentBuilder<C> builder) {
			ComponentFactory<?> component = getLast();
			this.x = chain.nextX(x, component.getWidth(), builder.getWidth());
			this.y = chain.nextY(y, component.getHeight(), builder.getHeight());
			append(ComponentFactory.wrap(builder, x, y));
			return this;
		}

		@Override
		public Builder inset(float margin) {
			throw new UnsupportedOperationException("Unable to set inset for UiComposed!");
		}

		@Override
		public Builder inset(float top, float bottom, float right, float left) {
			throw new UnsupportedOperationException("Unable to set inset for UiComposed!");
		}

		@Override
		public Builder box(int w, int h) {
			throw new UnsupportedOperationException("Unable to set bounding box for UiComposed!");
		}

		@Override
		public int getWidth() {
			return maxX - minX;
		}

		@Override
		public int getHeight() {
			return maxY - minY;
		}

		@Override
		protected Builder self() {
			return this;
		}

		@Override
		public UiComposed build(GridContext context, int x, int y) {
			return new UiComposed(context, minX + x, minY + y, getWidth(), getHeight(), components.stream().map(factory -> factory.build(context, x, y)).toList());
		}

	}

}
