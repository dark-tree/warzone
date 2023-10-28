package net.darktree.warzone.client.render.image;

import net.darktree.warzone.client.json.NinePatchJsonBlob;
import net.darktree.warzone.util.Logger;
import net.darktree.warzone.util.Resources;

import java.util.ArrayList;
import java.util.List;

public class SpriteLoader {

	private Atlas atlas = null;
	private Sprite missingno = null;
	private List<NamedSprite> sprites = new ArrayList<>();

	/**
	 * Get a sprite of the given image path
	 * The returned object will be usable only after a call to {@link SpriteLoader#load(Atlas)}
	 */
	public Sprite getSprite(String path) {
		// if atlas is already loaded make a direct request
		if (atlas != null) {
			return loadSprite(missingno, atlas, path);
		}

		// defer sprite loading and return a dummy wrapper
		WrappedSprite sprite = new WrappedSprite(Sprite.NULL);
		sprites.add(new NamedSprite(path, sprite));
		return sprite;
	}

	public NinePatch getNinePatch(String identifier) {
		Sprite[] sprites = new Sprite[9];

		for (int y = 0; y <= 2; y ++) {
			for (int x = 0; x <= 2; x ++) {
				sprites[x + y * 3] = getSprite(identifier + "/" + x + y + ".png");
			}
		}

		try {
			NinePatchJsonBlob blob = Resources.json("sprites/" + identifier + "/index.json", NinePatchJsonBlob.class);
			return new NinePatch(sprites, blob.width, blob.height, blob.edge);
		} catch (Exception e) {
			return new NinePatch(sprites, 20, 20, 20);
		}
	}

	/**
	 * Loads all the requested sprites, after this call all the returned sprites
	 * will become ready for usage.
	 */
	public void load(Atlas atlas) {
		this.atlas = atlas;

		try {
			missingno = atlas.getSprite("missingno.png");
		} catch (Exception e) {
			throw new RuntimeException("Failed to load fallback sprite!", e);
		}

		for (NamedSprite holder : sprites) {
			holder.sprite.setSprite(loadSprite(missingno, atlas, holder.path));
		}

		// free memory
		sprites = null;
	}

	private Sprite loadSprite(Sprite fallback, Atlas atlas, String path) {
		try {
			return atlas.getSprite(path);
		} catch (Exception e) {
			Logger.warn("Failed to load texture '", path, "', using missing texture!");
		}

		return fallback;
	}

	private record NamedSprite(String path, WrappedSprite sprite) {}

}
