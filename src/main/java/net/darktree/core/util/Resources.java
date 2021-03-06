package net.darktree.core.util;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.stream.Stream;

public class Resources {

	private static final ClassLoader LOADER = Thread.currentThread().getContextClassLoader();
	private static final String WORKPLACE = System.getProperty("user.dir");

	public static String path() {
		return WORKPLACE;
	}

	public static Path location(String path) {
		try {
			Path location = Path.of(Objects.requireNonNull(LOADER.getResource(path)).toURI());

			if( Files.exists(location) ) {
				return location;
			}
		}catch (Exception ignored) {
		}

		Path of = Path.of(WORKPLACE + "/" + path);
		if (Files.exists(of)) {
			return of;
		}

		Logger.warn("Failed to locate resource ", path);
		return null;
	}

	public static String contents(String path) throws IOException {
		try {
			return Files.readString(Objects.requireNonNull(location(path)));
		} catch (IOException exception) {
			Logger.error("Requested resource '", path, "' could not be found!");
			throw exception;
		}
	}

	public static Stream<Path> listing(String path) throws IOException {
		return Files.list(Objects.requireNonNull(location(path)));
	}

	public static JsonElement json(String path) throws IOException {
		try {
			Reader reader = Files.newBufferedReader(Objects.requireNonNull(location(path)));
			JsonElement element = JsonParser.parseReader(reader);
			reader.close();
			return element;
		}catch (IOException exception) {
			Logger.error("Requested resource '", path, "' could not be found!");
			throw exception;
		}
	}

	public static <T> T json(String path, Class<T> clazz) throws IOException {
		try {
			Gson gson = new Gson();
			Reader reader = Files.newBufferedReader(Objects.requireNonNull(location(path)));
			return gson.fromJson(reader, clazz);
		}catch (IOException exception) {
			Logger.error("Requested resource '", path, "' could not be found!");
			throw exception;
		}
	}

}
