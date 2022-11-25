package net.darktree.warzone.util;

public abstract class ElementType<T extends ElementType<T>> {

	public abstract Registry<T> getRegistry();

	@SuppressWarnings("unchecked")
	public final int id() {
		return getRegistry().identifierOf((T) this);
	}

	@SuppressWarnings("unchecked")
	public final String key() {
		return getRegistry().keyOf((T) this);
	}

}
