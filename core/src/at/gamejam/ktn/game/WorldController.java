package at.gamejam.ktn.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import at.gamejam.ktn.game.entites.DelayBar;
import at.gamejam.ktn.game.entites.Item;
import at.gamejam.ktn.game.entites.PlayerSleep;
import at.gamejam.ktn.game.entites.PlayerWake;
import at.gamejam.ktn.game.entites.RedBull;
import at.gamejam.ktn.game.entites.Thesis;
import at.gamejam.ktn.game.entities.GameObject;
import at.gamejam.ktn.utils.CameraHelper;
import at.gamejam.ktn.utils.Constants;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class WorldController {
	public Sound					ingameMusic;
	public Sound					winMusic;
	public CameraHelper				cameraHelper;
	public PlayerSleep				playerSleep;
	public DelayBar					sleepBar;
	public PlayerWake				playerWake;
	public DelayBar					wakeBar;
	public long						timeElapsed;
	private World					b2World;
	private GeneratedLevel			level;
	private float					lastSpawn		= 0;
	private final boolean			debug			= true;
	private InputManager			inputManager;
	private final List<GameObject>	objectsToAdd	= new ArrayList<GameObject>();
	private MyContactListener		contactListener;

	public WorldController() {
		this.init();
	}

	public void init() {

		this.cameraHelper = new CameraHelper();
		// this.b2World = new World(new Vector2(0, -9.81f), true);
		this.b2World = new World(new Vector2(0, 0), true); // no gravity

		this.playerSleep = new PlayerSleep(new Vector2(-1.5f, -1.0f), this);
		this.playerSleep.setName("PlayerSleep");
		this.sleepBar = new DelayBar(this.playerSleep);
		this.playerWake = new PlayerWake(new Vector2(1.5f, -1.0f), this);
		this.playerWake.setName("PlayerWake");
		this.wakeBar = new DelayBar(this.playerWake);
		// this.cameraHelper.setTarget(this.playerSleep.getB2Body());
		this.cameraHelper.setTarget(new Vector2(this.playerSleep.position.x - this.playerWake.position.x, this.playerSleep.position.y - this.playerWake.position.y));
		// Vector(x2-x1,y2-y1
		// this.level = new Level(this.b2World);

		this.level = new GeneratedLevel(this.b2World);
		this.contactListener = new MyContactListener(this);
		this.b2World.setContactListener(this.contactListener);
		this.inputManager = new InputManager(this.playerWake, this.playerSleep, this.cameraHelper);
		Gdx.input.setInputProcessor(this.inputManager);
		/*midiPlayer.open(Constants.MUSIC);
		midiPlayer.setLooping(true);
		midiPlayer.setVolume(0.5f);
		midiPlayer.play();*/
	}

	public void update(final float deltaTime) {
		// remove objects before or after step!!
		if (this.contactListener.getObjectsToRemove().size() > 0) {
			for (final GameObject object : this.contactListener.getObjectsToRemove()) {
				if (object.toDelete) {
					this.b2World.destroyBody(object.getB2Body());
					object.setToRender(false);
					GameObject.totalObjects.remove(object);
					if (object instanceof Thesis) {
						Thesis.itemCount--;
					}
					if (object instanceof RedBull) {
						RedBull.itemCount--;
					}
				}
			}
			this.contactListener.getObjectsToRemove().clear();
		}

		this.timeElapsed += deltaTime * 1000;
		this.b2World.step(1 / 60f, 3, 8); // timeStep, velocityIteration, positionIteration

		// this.cameraHelper.update(deltaTime);
		final Vector2 vector = new Vector2();
		vector.x = (this.playerWake.position.x + this.playerSleep.position.x) / 2;
		vector.y = (this.playerWake.position.y + this.playerSleep.position.y) / 2;
		this.cameraHelper.update(vector);
		final double temp1 = Math.pow((this.playerWake.position.x - this.playerSleep.position.x), 2);
		final double temp2 = Math.pow((this.playerWake.position.y - this.playerSleep.position.y), 2);
		final double distance = Math.sqrt(temp1 + temp2);
		// System.out.println(distance);
		final double factor = 0.2;
		/*if (distance < 5) {
			factor = factor * (-1);
		}*/
		this.cameraHelper.setZoom((float) (distance * factor));
		// System.out.println(distance);

		/*for (final GameObject o : this.level.getGameObjects()) {
			if (o instanceof Item) {
				final Item b = (Item) o;
				if (b.isCollected() && !b.destroyed) {
					this.b2World.destroyBody(b.getB2Body());
					b.destroyed = true;
					// b.toDelete = true;
				}
			}
		}*/

		this.createDynamicObjects();

		this.playerSleep.update(deltaTime);
		this.playerWake.update(deltaTime);
		this.sleepBar.update(deltaTime);
		this.wakeBar.update(deltaTime);
		this.level.update(deltaTime);
		/*if (this.player.getBody().getPosition().y < -3) {
			this.reset = true;
		}	if (this.reset) {
			this.reset();
		}*/
		// this.testCoins();

		for (final Item item : Item.itemList) {
			if (item.isFlying) {
				item.flyingTime += deltaTime;
			}
			if (item.flyingTime > 0.8) {
				item.collectable = true;
			}
		}

		boolean spawn = false;
		this.lastSpawn = this.lastSpawn + deltaTime;
		if (this.lastSpawn > 10) {
			final Random rnd = new Random();
			final float i = rnd.nextFloat();
			if (RedBull.itemCount < 10) {
				spawn = true;
				this.addTempGameObject(new RedBull(new Vector2(2f + i, 2f), this.b2World, true));
				this.addTempGameObject(new RedBull(new Vector2(-1.5f, 1f + i), this.b2World, true));
			}
		}
		if (this.lastSpawn > 10) {
			final Random rnd = new Random();
			final float i = rnd.nextFloat();
			if (Thesis.itemCount < 10) {
				spawn = true;
				this.addTempGameObject(new Thesis(new Vector2(2f + i, -2.5f), this.b2World, true));
				this.addTempGameObject(new Thesis(new Vector2(-2f, -1.5f + i), this.b2World, true));
			}
		}
		if (spawn) {
			this.lastSpawn = 0;
		}
	}

	/**
	 *
	 */
	public void createDynamicObjects() {
		// add objects after step!!
		for (final GameObject object : this.contactListener.getObjectsToAdd()) {
			object.setToRender(true);
			this.level.addGameObject(object);
			object.initPhysics();
		}
		this.contactListener.getObjectsToAdd().clear();
		// add objects after step!!
		for (final GameObject object : this.objectsToAdd) {
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

	protected GeneratedLevel getLevel() {
		return this.level;
	}

	/*protected void addAbstractItem(final GameObject object) {
	this.level.addGameObject(object);
	}*/

	public InputManager getInputManager() {
		return this.inputManager;
	}

	public boolean loadMusic() {
		this.ingameMusic = Gdx.audio.newSound(Gdx.files.internal(Constants.MUSIC2));
		this.winMusic = Gdx.audio.newSound(Gdx.files.internal(Constants.VICTORY));
		if (this.ingameMusic != null) {
			this.ingameMusic.play();
			this.ingameMusic.setVolume(1, 0.5f);
		}
		return true;
	}
}
