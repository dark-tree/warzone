package net.darktree.warzone.client.gui.prefab;

import net.darktree.warzone.client.gui.GridContext;

public class GridPrefabs {

	public static final GridContextFactory NONE = () -> null;
	public static final GridContextFactory POPUP = () -> new GridContext(18, 10);
	public static final GridContextFactory STANDARD = () -> new GridContext(39, 23);
	public static final GridContextFactory FREEPLAY = () -> new GridContext(39, 24);

}
