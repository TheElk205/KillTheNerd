package at.gamejam.ktn.game;

import java.util.ArrayList;
import java.util.List;

import at.gamejam.ktn.game.entites.Item;
import at.gamejam.ktn.game.entites.PlayerSleep;
import at.gamejam.ktn.game.entites.PlayerWake;
import at.gamejam.ktn.game.entities.GameObject;
import at.gamejam.ktn.utils.CameraHelper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

public class WorldController {
	public CameraHelper				cameraHelper;
	public PlayerSleep				playerSleep;
	public PlayerWake				playerWake;
	public long						timeElapsed;
	protected int					coinCount		= 0;
	private World					b2World;
	private TopDownLevel			level;
	private final boolean			debug			= true;
	// private boolean reset;

	private final List<GameObject>	objectsToAdd	= new ArrayList<GameObject>();
	private MyContactListener		contactListener;

	public WorldController() {
		this.init();
	}

	public void init() {

		this.cameraHelper = new CameraHelper();
		// this.b2World = new World(new Vector2(0, -9.81f), true);
		this.b2World = new World(new Vector2(0, 0), true); // no gravity

		this.playerSleep = new PlayerSleep(new Vector2(0.5f, 1.5f), this);
		this.playerWake = new PlayerWake(new Vector2(1.5f, 0.5f), this);

		// this.cameraHelper.setTarget(this.playerSleep.getBody());
		// this.level = new Level(this.b2World);

		this.level = new TopDownLevel(this.b2World);
		contactListener = new MyContactListener(this);
		this.b2World.setContactListener(contactListener);
		Gdx.input.setInputProcessor(new InputManager(this.playerSleep, this.playerWake, this.cameraHelper));
	}

	public void update(final float deltaTime) {
		// Add Items

		this.timeElapsed += deltaTime * 1000;
		this.b2World.step(1 / 60f, 3, 8); // timeStep, velocityIteration, positionIteration

		// remove objects after step!!
		for (GameObject object : this.contactListener.getObjectsToRemove()) {
			object.setToRender(false);
			final Body body = object.getB2Body();
			this.b2World.destroyBody(body);
		}
		this.contactListener.getObjectsToRemove().clear();

		this.cameraHelper.update(deltaTime);
		for (GameObject o : this.level.getGameObjects()) {
			if (o instanceof Item) {
				Item b = (Item) o;
				if (b.isCollected() && !b.destroyed) {
					this.b2World.destroyBody(b.getBody());
					b.destroyed = true;
				}
			}
		}

		this.createDynamicObjects();

		this.playerSleep.update(deltaTime);
		this.playerWake.update(deltaTime);
		this.level.update(deltaTime);
		/*if (this.player.getBody().getPosition().y < -3) {
			this.reset = true;
		}	if (this.reset) {
			this.reset();
		}*/
		// this.testCoins();
	}

	/**
	 *
	 */
	public void createDynamicObjects() {
		// add objects after step!!
		for (GameObject object : this.contactListener.getObjectsToAdd()) {
			object.setToRender(true);
			this.level.addGameObject(object);
			object.initPhysics();
		}
		this.contactListener.getObjectsToAdd().clear();
		// add objects after step!!
		for (GameObject object : this.objectsToAdd) {
			object.setToRender(true);
			this.level.addGameObject(object);
			// object.initPhysics();
		}
		this.objectsToAdd.clear();
	}

	/*private void reset() {
		this.reset = false;
		this.playerSleep.getBody().setTransform(new Vector2(0.5f, 1.5f), 0);
		this.playerSleep.getBody().setLinearVelocity(new Vector2(0, 0));
		this.playerSleep.getBody().setAngularVelocity(0);
		this.timeElapsed = 0;
		this.coinCount = 0;
	}*/

	public void addTempGameObject(final GameObject object) {
		this.objectsToAdd.add(object);
		// System.out.println("temp objectsToAdd:" + this.objectsToAdd.size());
	}

	public boolean isDebug() {
		return this.debug;
	}

	public World getB2World() {
		return this.b2World;
	}

	protected TopDownLevel getLevel() {
		return this.level;
	}

	/*protected void addAbstractItem(final GameObject object) {
		this.level.addGameObject(object);
	}*/
}
