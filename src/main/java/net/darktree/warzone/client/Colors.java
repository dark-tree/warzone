package net.darktree.warzone.client;

import net.darktree.warzone.client.render.color.Color;
import net.darktree.warzone.client.render.color.ColorAtlas;

public class Colors {

	private static final ColorAtlas ATLAS = ColorAtlas.load("colors.json");

	public static final Color NONE = ATLAS.getColor("none");
	public static final Color BORDER = ATLAS.getColor("border");
	public static final Color PATH = ATLAS.getColor("path");
	public static final Color OVERLAY_REACHABLE = ATLAS.getColor("overlay_reachable");
	public static final Color OVERLAY_NONE = ATLAS.getColor("overlay_none");
	public static final Color OVERLAY_FOREIGN = ATLAS.getColor("overlay_foreign");
	public static final Color BUTTON_HOVER = ATLAS.getColor("button_hover");
	public static final Color BUTTON_PRESSED = ATLAS.getColor("button_pressed");
	public static final Color BUTTON_DEFAULT = ATLAS.getColor("button_default");
	public static final Color BUTTON_INACTIVE = ATLAS.getColor("button_inactive");
	public static final Color SPOT_VALID = ATLAS.getColor("spot_valid");
	public static final Color SPOT_INVALID = ATLAS.getColor("spot_invalid");
	public static final Color ENTITY_SELECTION = ATLAS.getColor("entity_selection");
	public static final Color SCREEN_SEPARATOR = ATLAS.getColor("screen_separator");
	public static final Color TOO_EXPENSIVE = ATLAS.getColor("too_expensive");
	public static final Color PRICE_TAG = ATLAS.getColor("price_tag");
	public static final Color CARD_SEPARATOR = ATLAS.getColor("card_separator");
	public static final Color CARD_SEPIA = ATLAS.getColor("card_sepia");
	public static final Color CARD_BACKGROUND = ATLAS.getColor("card_background");
	public static final Color MAP_LIST_DEFAULT = ATLAS.getColor("map_list_default");
	public static final Color MAP_LIST_HOVER = ATLAS.getColor("map_list_hover");
	public static final Color MAP_LIST_SELECTED = ATLAS.getColor("map_list_selected");

}
