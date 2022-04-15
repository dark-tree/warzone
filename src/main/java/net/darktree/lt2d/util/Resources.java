package net.darktree.lt2d.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class Resources {

	private static final ClassLoader LOADER = Thread.currentThread().getContextClassLoader();
	private static final String WORKPLACE = System.getProperty("user.dir");

	public static String path() {
		return WORKPLACE;
	}

	public static Path location(String path) {
		try {
			Path location = Path.of(Objects.requireNonNull(LOADER.getResource(path)).getPath());

			if( Files.exists(location) ) {
				return location;
			}else{
				return Path.of(WORKPLACE + "/" + path);
			}
		}catch (Exception exception) {
			Logger.warn("Failed to locate resource ", path);
		}

		return null;
	}

	public static String contents(String path) throws IOException {
		try {
			return Files.readString(location(path));
		} catch (IOException exception) {
			Logger.error("Requested resource '", path, "' could not be found!");
			throw exception;
		}
	}

}
