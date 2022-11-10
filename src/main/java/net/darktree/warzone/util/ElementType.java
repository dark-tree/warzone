package net.darktree.warzone.util;

public abstract class ElementType<T> {

	public abstract Registry<T> getRegistry();

	@SuppressWarnings("unchecked")
	public final int id() {
		return getRegistry().identifierOf((T) this);
	}

}
