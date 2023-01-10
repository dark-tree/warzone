package net.darktree.warzone.country;

import net.darktree.warzone.client.render.image.Sprite;

public interface Purchasable {
	String getName();
	String getDescription();
	Sprite getIcon();
	int getCost();
}
