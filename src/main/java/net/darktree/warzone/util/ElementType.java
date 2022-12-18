package net.darktree.warzone.util;

public abstract class ElementType<T extends ElementType<T>> {

	public abstract Registry<T> getRegistry();

	/**
	 * Get the numerical id of this element
	 */
	@SuppressWarnings("unchecked")
	public final int id() {
		return getRegistry().byValue((T) this).id();
	}

	/**
	 * Get the string key of this element
	 */
	@SuppressWarnings("unchecked")
	public final String key() {
		return getRegistry().byValue((T) this).key();
	}

	public void onRegister(String id) {

	}

}
