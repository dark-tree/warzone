package net.darktree.warzone.client.text;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.darktree.warzone.util.Logger;
import net.darktree.warzone.util.Resources;

import java.util.HashMap;
import java.util.IllegalFormatException;
import java.util.Map;

public class Language {

	public static final String FALLBACK = "en_US";
	private final Map<String, String> translations;

	public Language(HashMap<String, String> translations) {
		this.translations = translations;
	}

	private static void loadKeySet(HashMap<String, String> entries, String base, JsonObject object) {
		for (String key : object.keySet()) {
			final JsonElement element = object.get(key);
			final String absolute = base.isEmpty() ? key : base + "." + key;

			if (element.isJsonPrimitive()) {
				entries.put(absolute, element.getAsString());
				continue;
			}

			if (element.isJsonObject()) {
				loadKeySet(entries, absolute, (JsonObject) element);
				continue;
			}

			Logger.warn("Invalid language entry for key '", absolute, "'!");
		}
	}

	public static Language load(String name) {
		try {
			JsonObject object = Resources.json("lang/" + name + ".json").getAsJsonObject();
			HashMap<String, String> lang = new HashMap<>();
			loadKeySet(lang, "", object);

			return new Language(lang);
		} catch (Exception e) {
			if (!FALLBACK.equals(name)) {
				Logger.error("Unable to load language '", name, "'! Attempting to fallback to '", FALLBACK, "'...");
				return Language.load(FALLBACK);
			} else {
				Logger.error("Failed to load any translation!");
			}

			return new Language(new HashMap<>());
		}
	}

	public String get(String key) {
		return translations.getOrDefault(key, key);
	}

	public boolean has(String key) {
		return translations.containsKey(key);
	}

	public String formatted(String key, Object... values) {
		return format(get(key), values);
	}

	public static String format(String value, Object... args) {
		try {
			return value.formatted(args);
		} catch (IllegalFormatException e) {
			return "*ERROR*";
		}
	}

}
