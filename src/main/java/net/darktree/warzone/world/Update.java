package net.darktree.warzone.world;

import org.intellij.lang.annotations.MagicConstant;

/**
 * A set of flags used to communicate a need to recalculate and/or redraw
 * something to the world. Used by {@link WorldSnapshot#pushUpdateBits}.
 */
public class Update {

	/**
	 * Causes the control calculation to be re-run, this flag is already
	 * included in {@link Update#OWNER}, and should only be used on its own if the
	 * territorial control changed without ANY changes to the country borders.
	 */
	public static final int CONTROL  = 0b00001;

	/**
	 * A combination of {@link Update#CONTROL} and {@link Update#BORDER},
	 * this flag should be used if the border of a country changed as that
	 * necessitates a border AND control recalculation.
	 */
	public static final int OWNER    = 0b00011;

	/**
	 * Can't be used on its own, for border changes use {@link Update#OWNER}.
	 * This flags marks that only the actual border of a country should be updated
	 * and redrawn, but it does not update the controlled area cache.
	 */
	public static final int BORDER   = 0b00010;

	/**
	 * Marks that the world surface updated and needs to be redrawn.
	 * During normal play this flag should not be emitted, it can however be
	 * used during world editing.
	 */
	public static final int SURFACE  = 0b00100;

	/**
	 * Marks that the world overlay needs to be recalculated and redrawn.
	 * This is used by units to update the movement overlay when
	 * they equip/un-equip armor.
	 */
	public static final int OVERLAY  = 0b01000;

	/**
	 * Marks that some building was updated and needs to be redrawn.
	 * This is used by buildings when placed and removed, and notifies
	 * the world renderer that it needs to redraw them.
	 */
	public static final int BUILDING = 0b10000;

	/**
	 * Update everything. This flag set is used during world loading.
	 */
	public static final int ALL      = 0b11111;

	@MagicConstant(flags = {CONTROL, OWNER, SURFACE, OVERLAY, BUILDING, ALL})
	public @interface Flags {}
}
