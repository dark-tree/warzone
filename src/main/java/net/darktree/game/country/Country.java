package net.darktree.game.country;

import java.util.HashMap;

public class Country {
	private static final HashMap<Symbol, Country> COUNTRIES = new HashMap<>();

	private final Symbol symbol;

	private Country(Symbol symbol) {
		this.symbol = symbol;
	}

	public static Country create(Symbol symbol) {
		return COUNTRIES.put(symbol, new Country(symbol));
	}

	public static Country of(Symbol symbol) {
		return COUNTRIES.get(symbol);
	}

	public static void reset() {
		COUNTRIES.clear();
	}
}
