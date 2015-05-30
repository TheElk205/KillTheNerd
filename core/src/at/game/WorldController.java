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

import at.game.enums.ItemType;
import at.game.enums.PlayerType;
import at.game.managers.InputManager;
import at.game.utils.CameraHelper;
import at.game.visuals.GameObject;
import at.game.visuals.Item;
import at.game.visuals.Player;
import at.game.visuals.tiles.GeneratedLevel;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

public class WorldController {
	private static final boolean			spawnEnabled	= true;
	private static CameraHelper				cameraHelper;
	protected static Player					playerSleep;
	protected static Player					playerWake;

	private static long						timeElapsed;
	public static World						topDown_b2World;
	private static GeneratedLevel			level;
	private static float					lastSpawn		= 0;
	private static final boolean			debug			= false;
	private static InputManager				inputManager;
	private static final List<GameObject>	objectsToAdd	= new ArrayList<GameObject>();
	private static MyContactListener		contactListener;
	private static double					distance;
	// private final static Logger LOGGER = new Logger("WorldController-Logger", Logger.INFO);
	private final static Logger				LOGGER			= Logger.getLogger(WorldController.class.getName());

	protected WorldController() {
		if (WorldController.debug) {
			WorldController.LOGGER.info("test info");
		}
		WorldController.cameraHelper = new CameraHelper();
		// this.b2World = new World(new Vector2(0, -9.81f), true);
		WorldController.topDown_b2World = new World(new Vector2(0, 0), true); // no gravity

		WorldController.playerSleep = new Player(new Vector2(-1.5f, -1.0f), ItemType.Sleep_Item, -10, PlayerType.Sleep);
		WorldController.playerSleep.setName("PlayerSleep");
		WorldController.playerWake = new Player(new Vector2(1.5f, -1.0f), ItemType.Wake_Item, +10, PlayerType.Wake);
		WorldController.playerWake.setName("PlayerWake");
		// this.cameraHelper.setTarget(this.playerSleep.getB2Body());
		WorldController.cameraHelper.setTarget(new Vector2(WorldController.playerSleep.position.x - WorldController.playerWake.position.x, WorldController.playerSleep.position.y
				- WorldController.playerWake.position.y));
		// Vector(x2-x1,y2-y1
		// this.level = new Level(this.b2World);

		WorldController.level = new GeneratedLevel(WorldController.topDown_b2World);
		WorldController.contactListener = new MyContactListener();
		WorldController.topDown_b2World.setContactListener(WorldController.contactListener);
		WorldController.inputManager = new InputManager(WorldController.playerWake, WorldController.playerSleep, WorldController.cameraHelper);
		Gdx.input.setInputProcessor(WorldController.inputManager);
		/*midiPlayer.open(Constants.MUSIC);
		midiPlayer.setLooping(true);
		midiPlayer.setVolume(0.5f);
		midiPlayer.play();*/
		if (WorldController.debug) {
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
			e.printStackTrace();
		}
		// create a TXT formatter
		final SimpleFormatter formatterTxt = new SimpleFormatter();
		fileTxt.setFormatter(formatterTxt);
		WorldController.LOGGER.addHandler(fileTxt);
		WorldController.LOGGER.info("test info 2");
	}

	protected void update(final float deltaTime) {
		// remove objects before or after step!!
		final List<GameObject> removeList = WorldController.contactListener.getObjectsToRemove();
		if (removeList.size() > 0) {
			for (final GameObject object : removeList) {
				if (object.toDelete) {
					WorldController.topDown_b2World.destroyBody(object.getB2Body());
					object.setToRender(false);
					GameObject.totalObjects.remove(object);
					/*if (object.getType == Item2) {
						Thesis.itemCount--;
					}
					if (object.getType == Item1) {
						RedBull.itemCount--; // Item1 = REDBULL
					}*/
				}
			}
			WorldController.contactListener.getObjectsToRemove().clear();
		}

		WorldController.timeElapsed += deltaTime * 1000;
		WorldController.topDown_b2World.step(1 / 60f, 3, 8); // timeStep, velocityIteration, positionIteration

		// this.cameraHelper.update(deltaTime);
		final Vector2 vector = WorldController.calcPlayerDistance();
		WorldController.cameraHelper.update(vector);
		WorldController.cameraHelper.setZoom((float) (WorldController.distance * 0.2f));
		WorldController.createDynamicObjects();

		WorldController.playerSleep.update(deltaTime);
		WorldController.playerWake.update(deltaTime);
		WorldController.level.update(deltaTime);

		// check max player distance for camera
		/*this.printTime += deltaTime;
		if (this.printTime > 1) {
			this.printTime = 0;
			System.out.println(this.playerWake.position);
		}*/

		// verhindert dass sich die Spieler zuweit voneinander entfernen
		final double maxDistance = 14f;
		if (WorldController.distance > maxDistance) {
			final Body wakeBody = WorldController.playerWake.b2Body;
			final Body sleepBody = WorldController.playerSleep.b2Body;

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
				item.setFlyingTime(item.getFlyingTime() + deltaTime);
			}
			if (item.getFlyingTime() > 0.8) {
				item.setCollectable(true);
			}
		}

		if (WorldController.spawnEnabled) {
			final boolean spawn = false;
			WorldController.lastSpawn = WorldController.lastSpawn + deltaTime;
			if (WorldController.lastSpawn > 10) {
				final Random rnd = new Random();
				final float i = rnd.nextFloat();
				/*if (RedBull.itemCount < 10) { // TODO class itemManager which holds all instances and counts, in class Item
					spawn = true;
					this.addTempGameObject(new RedBull(new Vector2(2f + i, 2f), true));
					this.addTempGameObject(new RedBull(new Vector2(-1.5f, 1f + i), true));
				}*/
			}
			if (WorldController.lastSpawn > 10) {
				final Random rnd = new Random();
				final float i = rnd.nextFloat();
				/*if (Thesis.itemCount < 10) {
					spawn = true;
					this.addTempGameObject(new Thesis(new Vector2(2f + i, -2.5f), true));
					this.addTempGameObject(new Thesis(new Vector2(-2f, -1.5f + i), true));
				}*/
			}
			if (spawn) {
				WorldController.lastSpawn = 0;
			}
		}
	}

	protected static Vector2 calcPlayerDistance() {
		final Vector2 vector = new Vector2();
		vector.x = (WorldController.playerWake.position.x + WorldController.playerSleep.position.x) / 2;
		vector.y = (WorldController.playerWake.position.y + WorldController.playerSleep.position.y) / 2;
		final double temp1 = Math.pow((WorldController.playerWake.position.x - WorldController.playerSleep.position.x), 2);
		final double temp2 = Math.pow((WorldController.playerWake.position.y - WorldController.playerSleep.position.y), 2);
		WorldController.distance = Math.sqrt(temp1 + temp2);
		return vector;
	}

	protected static double calcPossibleXPlayerDistance() {
		final Vector2 vector = new Vector2();
		int xOffset = 2;
		int yOffset = 2;
		if (WorldController.playerWake.position.x < WorldController.playerSleep.position.x) {
			xOffset = -2;
		}
		if (WorldController.playerWake.position.y < WorldController.playerSleep.position.y) {
			yOffset = -2;
		}

		vector.x = (WorldController.playerWake.position.x + xOffset + WorldController.playerSleep.position.x) / 2;
		vector.y = (WorldController.playerWake.position.y + yOffset + WorldController.playerSleep.position.y) / 2;
		final double temp1 = Math.pow(((WorldController.playerWake.position.x + xOffset) - WorldController.playerSleep.position.x), 2);
		final double temp2 = Math.pow(((WorldController.playerWake.position.y + yOffset) - WorldController.playerSleep.position.y), 2);
		return Math.sqrt(temp1 + temp2);
	}

	protected static void createDynamicObjects() {
		// add objects after step!!
		for (final GameObject object : WorldController.contactListener.getObjectsToAdd()) {
			object.setToRender(true);
			WorldController.level.addGameObject(object);
			object.initPhysics();
		}
		WorldController.contactListener.getObjectsToAdd().clear();
		// add objects after step!!
		for (final GameObject object : WorldController.objectsToAdd) {
			object.setToRender(true);
			WorldController.level.addGameObject(object);
			// object.initPhysics();
		}
		WorldController.objectsToAdd.clear();
	}

	/*private void reset() {
		this.reset = false;
		this.playerSleep.getBody().setTransform(new Vector2(0.5f, 1.5f), 0);
		this.playerSleep.getBody().setLinearVelocity(new Vector2(0, 0));
		this.playerSleep.getBody().setAngularVelocity(0);
		this.timeElapsed = 0;
		this.coinCount = 0;
		}*/

	public static void addTempGameObject(final GameObject object) {
		WorldController.objectsToAdd.add(object);
		// System.out.println("temp objectsToAdd:" + this.objectsToAdd.size());
	}

	protected static boolean isDebug() {
		return WorldController.debug;
	}

	protected static World getB2World() {
		return WorldController.topDown_b2World;
	}

	protected static GeneratedLevel getLevel() {
		return WorldController.level;
	}

	protected static InputManager getInputManager() {
		return WorldController.inputManager;
	}

	protected static CameraHelper getCameraHelper() {
		return WorldController.cameraHelper;
	}

	protected static long getTimeElapsed() {
		return WorldController.timeElapsed;
	}
}
