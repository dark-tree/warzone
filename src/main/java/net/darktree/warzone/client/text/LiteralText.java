package net.darktree.warzone.client.text;

public class LiteralText extends Text {

	protected final String text;

	public LiteralText(String text) {
		this.text = text;
	}

	@Override
	public int length() {
		return text.length();
	}

	@Override
	public String str() {
		return text;
	}

}
