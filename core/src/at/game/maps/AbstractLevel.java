package at.game.maps;

import java.util.ArrayList;
import java.util.List;

import at.game.RenderObject;
import at.game.gamemechanics.Player;
import at.game.utils.CameraHelper;
import at.game.visuals.GameObject;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class AbstractLevel {
	protected Player					player1			= null;
	private final List<GameObject>		gameObjects		= new ArrayList<GameObject>();
	private final List<RenderObject>	levelObjects	= new ArrayList<RenderObject>();
	private final List<RenderObject>	renderObjects	= new ArrayList<RenderObject>();
	protected final CameraHelper		cameraHelper	= new CameraHelper();

	public Player getPlayer1() {
		return this.player1;
	}

	public abstract void init();

	/**
	 * called in WorldController
	 *
	 * @param object
	 */
	public void addGameObject(final GameObject object) {
		this.gameObjects.add(object);
	}

	/**
	 * TODO: eventuell denn rest auch updaten, zb animierte Tiles
	 *
	 * @param deltaTime
	 */
	public void update(final float deltaTime) {
		// System.out.println("AbstractLevel - update gameObjects:" + this.gameObjects.size());
		for (final GameObject gameObject : this.gameObjects) {
			// System.out.println("AbstractLevel - update: " + gameObject);
			gameObject.update(deltaTime);
		}
	}

	/**
	 * renders all lists of this class
	 *
	 * @param batch
	 */
	public void render(final SpriteBatch batch) {
		// System.out.println("AbstractLevel - render levelObjects:" + this.levelObjects.size());
		for (final RenderObject levelObject : this.levelObjects) { // must be rendered first
			levelObject.render(batch);
		}
		// System.out.println("AbstractLevel - render gameObjects:" + this.gameObjects.size());
		for (final GameObject gameObject : this.gameObjects) {
			gameObject.render(batch);
		}
		// System.out.println("AbstractLevel - render renderObjects:" + this.renderObjects.size());
		for (final RenderObject renderObject : this.renderObjects) {
			renderObject.render(batch);
		}
	}

	public CameraHelper getCameraHelper() {
		return this.cameraHelper;
	}

}
