package at.game.maps;

import java.util.ArrayList;
import java.util.List;

import at.game.RenderObject;
import at.game.mechanics.Player;
import at.game.objects.AbstractGameObject;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class AbstractLevel {
	protected Player						player1				= null;
	private final List<AbstractGameObject>	abstractGameObjects	= new ArrayList<AbstractGameObject>();
	private final List<RenderObject>		levelObjects		= new ArrayList<RenderObject>();
	private final List<RenderObject>		renderObjects		= new ArrayList<RenderObject>();
	// protected final CameraHelper cameraHelper = new CameraHelper();
	protected boolean						isInit				= false;

	public Player getPlayer1() {
		return this.player1;
	}

	public abstract void init();

	/**
	 * called in WorldController
	 *
	 * @param object
	 */
	public void addGameObject(final AbstractGameObject object) {
		this.abstractGameObjects.add(object);
	}

	/*
	 * TODO: eventuell denn rest auch updaten, zb animierte Tiles
	 *
	 * @param deltaTime

	public void update(final float deltaTime) {
		// System.out.println("AbstractLevel - update abstractGameObjects:" + this.gameObjects.size());
		for (final AbstractGameObject abstractGameObject : this.abstractGameObjects) {
			// System.out.println("AbstractLevel - update: " + gameObject);
			abstractGameObject.update(deltaTime);
		}
	}*/

	/**
	 * renders all lists of this class
	 *
	 * @param batch
	 */
	public void render(final SpriteBatch batch) {
		// System.out.println("AbstractLevel - render levelObjects:" + this.levelObjects.size());
		/*for (final RenderObject levelObject : this.levelObjects) { // must be rendered first
			levelObject.render(batch);
		}
		// System.out.println("AbstractLevel - render abstractGameObjects:" + this.gameObjects.size());
		for (final AbstractGameObject gameObject : this.gameObjects) {
			gameObject.render(batch);
		}
		// System.out.println("AbstractLevel - render renderObjects:" + this.renderObjects.size());
		for (final RenderObject renderObject : this.renderObjects) {
			renderObject.render(batch);
		}*/
	}

	/*public CameraHelper getCameraHelper() {
		return this.cameraHelper;
	}*/

}
