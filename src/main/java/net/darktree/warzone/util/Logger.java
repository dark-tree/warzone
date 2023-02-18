package net.darktree.warzone.util;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Logger {

	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

	private static String getTimestamp() {
		return LocalTime.now().format(FORMATTER);
	}

	private static void print(Level level, Object... objects) {
		StringBuilder builder = new StringBuilder(getTimestamp());
		builder.append(" ").append(level).append(" ");

		for (Object object : objects) {
			builder.append(object);
		}

		System.out.println(builder);
	}

	public static void info(Object... objects) {
		print(Level.INFO, objects);
	}

	public static void warn(Object... objects) {
		print(Level.WARN, objects);
	}

	public static void error(Object... objects) {
		print(Level.ERROR, objects);
	}

	public static void fatal(Object... objects) {
		print(Level.FATAL, objects);
	}

	private enum Level {
		INFO,
		WARN,
		ERROR,
		FATAL;

		@Override
		public String toString() {
			return super.toString() + ":";
		}
	}

}
