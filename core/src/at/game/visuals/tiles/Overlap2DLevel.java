package at.game.visuals.tiles;

import at.game.managers.AssetManager;

import com.uwsoft.editor.renderer.SceneLoader;
import com.uwsoft.editor.renderer.data.Essentials;
import com.uwsoft.editor.renderer.resources.IResourceRetriever;

/**
 * @author Herkt Kevin
 */
public class Overlap2DLevel {
	private final GameStage	stage;

	/**
	 *
	 */
	public Overlap2DLevel() {
		final Essentials essentials = new Essentials();
		final IResourceRetriever assetManager = new AssetManager();
		essentials.rm = assetManager;

		final SceneLoader sceneLoader = new SceneLoader(essentials);
		sceneLoader.loadScene("MainScene"); // Gdx.files.internal( ../core/assets/OverlapDemo/scenes/

		this.stage = new GameStage(sceneLoader);
	}

	/**
	 * @return state the GameStage
	 */
	public GameStage getGameStage() {
		return this.stage;
	}
}
