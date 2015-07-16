package at.game.managers;

import at.game.Constants;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Disposable;

public class Assets implements Disposable, AssetErrorListener {
	private static Assets	instance;
	private AssetManager	assetManager;
	private TextureAtlas	atlas;

	private Assets(final AssetManager assetManager) {
		this.init(assetManager);
	}

	public static Assets getInstance() {
		if (Assets.instance == null) {
			final AssetManager am = new AssetManager();
			Assets.instance = new Assets(am);
		}
		return Assets.instance;
	}

	private void init(final AssetManager assetManager) {
		this.assetManager = assetManager;
		this.assetManager.setErrorListener(this);
		assetManager.load(Constants.ATLAS_FOLDER + Constants.ATLAS_NAME + ".atlas", TextureAtlas.class);
		assetManager.finishLoading();
		this.atlas = assetManager.get(Constants.ATLAS_FOLDER + Constants.ATLAS_NAME + ".atlas");
		for (final Texture t : this.atlas.getTextures()) {
			t.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear); // linear filter for smoothing pixels
			// Gdx.app.debug(Assets.class.getName(), t.toString());
			// System.out.println("Assets: " + t.toString());
		}

		// sollte vl direkt in einer Level instanz aufgerufen werden
		this.initLevel1();
	}

	private void initLevel1() {
		AssetStore.background = this.findRegion("backgroundSwamp");
	}

	/**
	 * this is a expensive function, don't use it in a loop, like render or update!!
	 *
	 * @param name
	 * @return TextureAtlas
	 */
	public TextureAtlas.AtlasRegion findRegion(final String name) {
		final AtlasRegion region = this.atlas.findRegion(name);
		if (region == null) {
			throw new IllegalArgumentException(name + " not found");
		}
		return region;
	}

	@Override
	public void dispose() {
		this.assetManager.dispose();
		Assets.instance = null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void error(final AssetDescriptor asset, final Throwable throwable) {
		Gdx.app.error(Assets.class.getName(), "Couldn't load asset '" + asset.fileName + "'", throwable);
	}
}
