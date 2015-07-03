package at.game.visuals.tiles;

import com.uwsoft.editor.renderer.Overlap2DStage;
import com.uwsoft.editor.renderer.SceneLoader;

/**
 * @author Herkt Kevin
 */
public class GameStage extends Overlap2DStage {

	/**
	 * @param sceneLoader
	 */
	public GameStage(final SceneLoader sceneLoader) {
		this.addActor(sceneLoader.getRoot());
		this.initSceneLoader();
		this.sceneLoader.loadScene("MainScene");
	}

}
