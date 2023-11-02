package net.darktree.warzone.client.gui.prefab;

import net.darktree.warzone.client.gui.GridContext;

public class GridPrefabs {

	public static final GridContextFactory NONE = () -> null;
	public static final GridContextFactory POPUP = () -> new GridContext(18, 10);

}
