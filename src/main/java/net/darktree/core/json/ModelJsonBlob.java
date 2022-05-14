package net.darktree.core.json;

import java.util.HashMap;
import java.util.Map;

public class ModelJsonBlob {
	public String[] sprites;
	public final Map<String, VariantJsonBlob[]> variants = new HashMap<>();

	public static class VariantJsonBlob {
		public int sprite;
		public int angle;
	}
}
