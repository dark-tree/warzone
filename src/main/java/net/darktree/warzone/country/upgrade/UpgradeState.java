package net.darktree.warzone.country.upgrade;

public class UpgradeState {

	public boolean value;

	public UpgradeState(boolean value) {
		this.value = value;
	}

	public void grant() {
		value = true;
	}

}
