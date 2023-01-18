package net.darktree.warzone.client.text;

import net.darktree.warzone.Main;

public class TranslatedText extends LiteralText {

	public TranslatedText(String text) {
		super(Main.lang.get(text));
	}

	@Override
	public String str() {
		return Main.lang.get(text);
	}

}
