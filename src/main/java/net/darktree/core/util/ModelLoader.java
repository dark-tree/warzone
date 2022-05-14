package net.darktree.core.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.darktree.core.json.ModelJsonBlob;

import java.io.IOException;

public class ModelLoader {

	public static ModelJsonBlob load(String string) {
		Gson gson = new Gson();

		try {
			JsonObject tag = Resources.json(string).getAsJsonObject();

			ModelJsonBlob blob = new ModelJsonBlob();
			blob.sprites = gson.fromJson(tag.get("sprites"), String[].class);

			tag.get("variants").getAsJsonObject().entrySet().forEach(entry -> {
				blob.variants.put(entry.getKey(), gson.fromJson(entry.getValue(), ModelJsonBlob.VariantJsonBlob[].class));
			});

			return blob;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
