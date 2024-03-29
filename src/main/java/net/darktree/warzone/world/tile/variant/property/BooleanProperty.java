package net.darktree.warzone.world.tile.variant.property;

public class BooleanProperty extends Property<Boolean> {

	private static final Boolean[] VALUES = new Boolean[] {false, true};
	private final String name;

	public BooleanProperty(String name) {
		this.name = name;
	}

	@Override
	public String name() {
		return this.name;
	}

	@Override
	public Boolean[] values() {
		return VALUES;
	}

	@Override
	public int indexOf(Boolean object) {
		if (object.equals(true)) return 1;
		if (object.equals(false)) return 0;

		throw new RuntimeException("Value does not belong to this property!");
	}
}
