package net.darktree.warzone.screen;

import net.darktree.warzone.client.gui.ModelBuilder;
import net.darktree.warzone.client.gui.component.UiButton;

public class ConfirmScreen extends AcceptScreen {

	private Runnable yes, no;

	public ConfirmScreen(CharSequence title, CharSequence message) {
		super(title, message);
	}

	public ConfirmScreen onYes(Runnable runnable) {
		this.yes = runnable;
		return this;
	}

	public ConfirmScreen onNo(Runnable runnable) {
		this.no = runnable;
		return this;
	}

	@Override
	protected void buildButtonModel(ModelBuilder builder) {
		builder.add(1, 1, UiButton.of(TEXT_YES.str()).box(6, 2).inset(0.1f, -0.2f).react(() -> runAction(true)));
		builder.add(11, 1, UiButton.of(TEXT_NO.str()).box(6, 2).inset(0.1f, -0.2f).react(() -> runAction(false)));
	}

	@Override
	public void onEscape() {
		runAction(false);
		super.close();
	}

	private void runAction(boolean state) {
		if (state && yes != null) yes.run();
		if (!state && no != null) no.run();
		close();
	}

}
