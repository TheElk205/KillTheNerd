package at.gamejam.ktn.utils;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Disposable;

public class Assets implements Disposable {

	private static Assets	instance;
	private AssetManager	assetManager;
	private TextureAtlas	atlas;

	private Assets(final AssetManager assetManager) {
		this.init(assetManager);
	}

	public static Assets getInstance(final AssetManager assetManager) {
		if (Assets.instance == null) {
			Assets.instance = new Assets(assetManager);
		}
		return Assets.instance;
	}

	public void init(final AssetManager assetManager) {
		this.assetManager = assetManager;
		assetManager.load("images/assets.atlas", TextureAtlas.class);
		assetManager.finishLoading();
		this.atlas = assetManager.get("images/assets.atlas");
		for (final Texture t : this.atlas.getTextures()) {
			t.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear); // linear filter for smoothing pixels
		}
	}

	public TextureAtlas.AtlasRegion findRegion(final String name) {
		return this.atlas.findRegion(name);
	}

	@Override
	public void dispose() {
		this.assetManager.dispose();
		Assets.instance = null;
	}
}
