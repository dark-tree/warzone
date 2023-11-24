package net.darktree.warzone.world.entity.building;

import net.darktree.warzone.world.WorldSnapshot;
import net.darktree.warzone.world.tile.TilePos;

import java.util.ArrayList;
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
	List<TilePos> getStructureTiles();

	default List<Building> getStructureParts(WorldSnapshot world) {
		List<Building> parts = new ArrayList<>();

		for (TilePos pos : getStructureTiles()) {
			Building part = world.getEntity(pos.x, pos.y, Building.class);

			if (part == null) {
				return null;
			}

			parts.add(part);
		}

		return parts;
	}

}
