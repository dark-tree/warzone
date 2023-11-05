package net.darktree.warzone.client.text;

/**
 * The CharSequence interface is implemented here only so that we have a common
 * superclass with String that's higher than Object to be able to call toString
 */
public abstract class Text implements CharSequence {

	public static final Text EMPTY = Text.literal("");

	@Override
	public abstract int length();
	public abstract String str();

	public String str(Object... args) {
		return args.length == 0 ? str() : Language.format(str(), args);
	}

	public static Text literal(String value) {
		return new LiteralText(value);
	}

	public static Text translated(String value) {
		return new TranslatedText(value);
	}

	@Override
	public String toString() {
		return str();
	}

	@Override
	public char charAt(int index) {
		throw new AssertionError();
	}

	@Override
	public CharSequence subSequence(int start, int end) {
		throw new AssertionError();
	}

}
