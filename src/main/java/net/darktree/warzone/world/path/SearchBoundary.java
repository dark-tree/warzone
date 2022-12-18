package net.darktree.warzone.world.path;

import net.darktree.warzone.country.Symbol;

public enum SearchBoundary {
	NONE,
	WITHIN,
	COLONIZED;

	static SearchBoundary getForUnitAt(boolean armored, Symbol owner) {
		return (armored || owner == Symbol.NONE) ? SearchBoundary.NONE : SearchBoundary.COLONIZED;
	}

	final boolean isValid(Symbol owner, Symbol self) {
		return this == NONE || owner == self || (this == COLONIZED && owner != Symbol.NONE);
	}
}
