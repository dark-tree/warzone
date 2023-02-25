package net.darktree.warzone.world.entity.building;

import net.darktree.warzone.world.tile.TilePos;

import java.util.List;

public interface MultipartStructure {

	/**
	 * Get a list of structure positions that all combine to form a single
	 * building/structure.
	 * <p>
	 *     This has the fallowing effect:
	 *     <ul>
	 *         <li>All parts of a structure are demolished/destroyed if one is demolished/destroyed</li>
	 *     </ul>
	 * </p>
	 */
	List<TilePos> getStructureParts();

}
