package at.game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import at.game.visuals.GameObject;
import at.game.visuals.hud.DelayBar;
import at.gamejam.ktn.game.entites.Item;
import at.gamejam.ktn.game.entites.PlayerSleep;
import at.gamejam.ktn.game.entites.PlayerWake;
import at.gamejam.ktn.game.entites.RedBull;
import at.gamejam.ktn.game.entites.Thesis;
import at.gamejam.ktn.utils.CameraHelper;
import at.gamejam.ktn.utils.Constants;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

public class WorldController {
	private static final boolean	spawnEnabled	= true;
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
	private final boolean			debug			= false;
	private InputManager			inputManager;
	private final List<GameObject>	objectsToAdd	= new ArrayList<GameObject>();
	private MyContactListener		contactListener;
	// private final float printTime = 0;
	private double					distance;
	// private final static Logger LOGGER = new Logger("WorldController-Logger", Logger.INFO);
	private final static Logger		LOGGER			= Logger.getLogger(WorldController.class.getName());

	public WorldController() {
		this.init();
	}

	public void init() {
		if (this.debug) {
			WorldController.LOGGER.info("test info");
		}
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
		if (this.debug) {
			WorldController.setUpLogger();
		}
	}

	private static void setUpLogger() {
		// LOGGER
		// get the global logger to configure it
		final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

		// suppress the logging output to the console
		final Logger rootLogger = Logger.getLogger("");
		final Handler[] handlers = rootLogger.getHandlers();
		if (handlers[0] instanceof ConsoleHandler) {
			rootLogger.removeHandler(handlers[0]);
		}
		logger.setLevel(Level.INFO);
		FileHandler fileTxt = null;
		try {
			fileTxt = new FileHandler("Logging.txt");
		} catch (SecurityException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// create a TXT formatter
		final SimpleFormatter formatterTxt = new SimpleFormatter();
		fileTxt.setFormatter(formatterTxt);
		WorldController.LOGGER.addHandler(fileTxt);
		WorldController.LOGGER.info("test info 2");
	}

	public void update(final float deltaTime) {
		// remove objects before or after step!!
		final List<GameObject> removeList = this.contactListener.getObjectsToRemove();
		if (removeList.size() > 0) {
			for (final GameObject object : removeList) {
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
		final Vector2 vector = this.calcPlayerDistance();
		this.cameraHelper.update(vector);
		this.cameraHelper.setZoom((float) (this.distance * 0.2f));
		this.createDynamicObjects();

		this.playerSleep.update(deltaTime);
		this.playerWake.update(deltaTime);
		this.sleepBar.update(deltaTime);
		this.wakeBar.update(deltaTime);
		this.level.update(deltaTime);

		// check max player distance for camera
		/*this.printTime += deltaTime;
		if (this.printTime > 1) {
			this.printTime = 0;
			System.out.println(this.playerWake.position);
		}*/

		// verhindert dass sich die Spieler zuweit voneinander entfernen
		final double maxDistance = 14f;
		if (this.distance > maxDistance) {
			final Body wakeBody = this.playerWake.b2Body;
			final Body sleepBody = this.playerSleep.b2Body;

			float wakeX = wakeBody.getPosition().x;
			float sleepX = sleepBody.getPosition().x;
			final float wakeY = wakeBody.getPosition().y;
			final float sleepY = sleepBody.getPosition().y;
			final float offset = 0.08f;
			if (wakeX > sleepX) {
				wakeX = wakeX - offset;
				sleepX = sleepX + offset;
			} else {
				wakeX = wakeX + offset;
				sleepX = sleepX - offset;
			}

			/*if (wakeY > sleepY) {
				wakeY = wakeY - offset;
				sleepY = sleepY + offset;
			} else {
				wakeY = wakeY + offset;
				sleepY = sleepY - offset;
			}*/
			wakeBody.setTransform(new Vector2(wakeX, wakeY), wakeBody.getAngle());
			sleepBody.setTransform(new Vector2(sleepX, sleepY), wakeBody.getAngle());
		}

		/*this.b2Body.setLinearVelocity(lin);
		this.b2Body.applyForceToCenter(toApply, true);

		this.position.x = this.position.x - 1;
		this.position.y = this.position.y + 3;
		if (this.calcPossibleXPlayerDistance() >= maxDistance) {
			this.position.x = this.position.x + 1;
			this.position.y = this.position.y - 3;
		}*/
		// this.b2Body.setLinearVelocity(new Vector2(-lin.x * 3, -lin.y * 3));

		for (final Item item : Item.itemList) {
			if (item.isFlying()) {
				item.flyingTime += deltaTime;
			}
			if (item.flyingTime > 0.8) {
				item.setCollectable(true);
			}
		}

		if (WorldController.spawnEnabled) {
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
	}

	public Vector2 calcPlayerDistance() {
		final Vector2 vector = new Vector2();
		vector.x = (this.playerWake.position.x + this.playerSleep.position.x) / 2;
		vector.y = (this.playerWake.position.y + this.playerSleep.position.y) / 2;
		final double temp1 = Math.pow((this.playerWake.position.x - this.playerSleep.position.x), 2);
		final double temp2 = Math.pow((this.playerWake.position.y - this.playerSleep.position.y), 2);
		this.distance = Math.sqrt(temp1 + temp2);
		return vector;
	}

	public double calcPossibleXPlayerDistance() {
		final Vector2 vector = new Vector2();
		int xOffset = 2;
		int yOffset = 2;
		if (this.playerWake.position.x < this.playerSleep.position.x) {
			xOffset = -2;
		}
		if (this.playerWake.position.y < this.playerSleep.position.y) {
			yOffset = -2;
		}

		vector.x = (this.playerWake.position.x + xOffset + this.playerSleep.position.x) / 2;
		vector.y = (this.playerWake.position.y + yOffset + this.playerSleep.position.y) / 2;
		final double temp1 = Math.pow(((this.playerWake.position.x + xOffset) - this.playerSleep.position.x), 2);
		final double temp2 = Math.pow(((this.playerWake.position.y + yOffset) - this.playerSleep.position.y), 2);
		return Math.sqrt(temp1 + temp2);
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
