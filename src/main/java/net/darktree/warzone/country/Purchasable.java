package net.darktree.warzone.country;

import net.darktree.warzone.client.render.image.Sprite;

public interface Purchasable {
	String getNameKey();
	String getDescriptionKey();
	Sprite getIcon();
	int getCost();
}
