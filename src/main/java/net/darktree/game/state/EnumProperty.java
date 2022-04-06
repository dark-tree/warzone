package net.darktree.game.state;

public class EnumProperty <T extends Enum<T>> extends Property<T> {

	private final Class<?> clazz;
	private final String name;

	public EnumProperty(Class<T> clazz, String name, T fallback) {
		super(fallback);
		this.clazz = clazz;
		this.name = name;
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public T[] values() {
		return (T[]) clazz.getEnumConstants();
	}

}
