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

import at.game.components.ControlledMovementSystem;
import at.game.maps.AbstractLevel;
import at.game.mechanics.Item;
import at.game.objects.AbstractGameObject;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

/**
 * @author Herkt Kevin
 */
public class WorldController {
	private float									accumulator		= 0;
	private long									timeElapsed;
	private final World								b2World;
	public static final float						GRAVITY			= -14f;
	// private static GeneratedLevel level;
	private float									lastSpawn		= 0;
	private final MyContactListener					contactListener;
	// private static double distanceBetweenSplitscreenPlayer;
	private final static Logger						LOGGER			= Logger.getLogger(WorldController.class.getName());
	private final static List<AbstractGameObject>	objectsToAdd	= new ArrayList<AbstractGameObject>();
	private final boolean							spawnEnabled	= true;
	private final AbstractLevel						level;
	public static final Engine						ashleyEngine	= new Engine();

	public WorldController(final AbstractLevel level) {
		System.out.println("WorldController - Constructor");
		WorldController.ashleyEngine.addSystem(new ControlledMovementSystem());
		this.level = level;
		if (Constants.DEBUG) {
			// LOGGER.info("test info");
		}

		this.b2World = new World(new Vector2(0, WorldController.GRAVITY), true); // with -10 gravity
		// genLevel = new GeneratedLevel();
		// genLevel.init();
		this.contactListener = new MyContactListener();
		this.b2World.setContactListener(this.contactListener);

		if (Constants.DEBUG) {
			WorldController.setUpLogger();
		}

		/*
		final BodyDef testBodyDef = new BodyDef();
		testBodyDef.position.set(0, -2); // -2meter height
		testBodyDef.type = BodyDef.BodyType.StaticBody;
		final Body body = getB2World().createBody(testBodyDef);
		final PolygonShape testBoxShape = new PolygonShape();
		testBoxShape.setAsBox(10f / 2, 1f / 2); // 10meter long platform
		final FixtureDef testFixtureDef = new FixtureDef();
		testFixtureDef.shape = testBoxShape;
		testFixtureDef.friction = 1f;
		// body.createFixture(testFixtureDef);
		testBoxShape.dispose();*/
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
			// create a TXT formatter
			final SimpleFormatter formatterTxt = new SimpleFormatter();
			fileTxt.setFormatter(formatterTxt);
			WorldController.LOGGER.addHandler(fileTxt);
			WorldController.LOGGER.info("test info 2");
		} catch (SecurityException | IOException e) {
			e.printStackTrace();
		}
	}

	public void update(final float deltaTime) {
		WorldController.ashleyEngine.update(deltaTime);

		// remove objects before or after step!!
		/*final List<AbstractGameObject> removeList = this.contactListener.getObjectsToRemove();
		if (removeList.size() > 0) {
			for (final AbstractGameObject object : removeList) {
				if (object.isToDelete()) {
					this.b2World.destroyBody(object.getB2Body());
					// object.setToRender(false);
					AbstractGameObject.getTotalObjects().remove(object);
				}
			}
			this.contactListener.getObjectsToRemove().clear();
		}*/

		this.timeElapsed += deltaTime * 1000;
		this.doPhysicsStep(deltaTime);

		// final Vector2 vector = calcPlayerDistance();
		// cameraHelper.update(vector);
		// cameraHelper.setZoom((float) (distance * 0.2f));
		// this.createDynamicObjects();

		// this.level.update(deltaTime);

		// check max player distance for camera
		/*this.printTime += deltaTime;
		if (this.printTime > 1) {
			this.printTime = 0;
			System.out.println(this.playerWake.position);
		}*/

		// verhindert dass sich die Spieler zuweit voneinander entfernen
		/*final double maxDistance = 14f;
		if (distance > maxDistance) {
			final Body wakeBody = playerWake.b2Body;
			final Body sleepBody = playerSleep.b2Body;

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

			wakeBody.setTransform(new Vector2(wakeX, wakeY), wakeBody.getAngle());
			sleepBody.setTransform(new Vector2(sleepX, sleepY), wakeBody.getAngle());
		}*/

		/*this.b2Body.setLinearVelocity(lin);
		this.b2Body.applyForceToCenter(toApply, true);

		this.position.x = this.position.x - 1;
		this.position.y = this.position.y + 3;
		if (this.calcPossibleXPlayerDistance() >= maxDistance) {
			this.position.x = this.position.x + 1;
			this.position.y = this.position.y - 3;
		}*/
		// this.b2Body.setLinearVelocity(new Vector2(-lin.x * 3, -lin.y * 3));

		for (final Item item : Item.getAllItems()) {
			if (item.isFlying()) {
				item.setFlyingTime(item.getFlyingTime() + deltaTime);
			}
			if (item.getFlyingTime() > 0.8) {
				item.setCollectable(true);
			}
		}

		this.spawn(deltaTime);
	}

	/**
	 * @param deltaTime
	 */
	public void spawn(final float deltaTime) {
		if (this.spawnEnabled) {
			final boolean spawn = false;
			this.lastSpawn = this.lastSpawn + deltaTime;
			if (this.lastSpawn > 10) {
				final Random rnd = new Random();
				final float i = rnd.nextFloat();
				/*if (RedBull.itemCount < 10) { // TODO class itemManager which holds all instances and counts, in class Item
					spawn = true;
					this.addTempGameObject(new RedBull(new Vector2(2f + i, 2f), true));
					this.addTempGameObject(new RedBull(new Vector2(-1.5f, 1f + i), true));
				}*/
			}
			if (this.lastSpawn > 10) {
				final Random rnd = new Random();
				final float i = rnd.nextFloat();
				/*if (Thesis.itemCount < 10) {
					spawn = true;
					this.addTempGameObject(new Thesis(new Vector2(2f + i, -2.5f), true));
					this.addTempGameObject(new Thesis(new Vector2(-2f, -1.5f + i), true));
				}*/
			}
			if (spawn) {
				this.lastSpawn = 0;
			}
		}
	}

	/**
	 * @param deltaTime
	 */
	private void doPhysicsStep(final float deltaTime) {
		/*while (accumulator > FPSCAP) {
			if ((accumulator - FPSCAP) > FPSCAP) {
				b2World.step(FPSCAP, 1, 1); // 1/60f timeStep, velocityIteration, positionIteration
			} else {
				b2World.step(accumulator - FPSCAP, 1, 1); // 1/60f timeStep, velocityIteration,
			}
			accumulator -= FPSCAP;
		}*/
		final float frameTime = Math.min(deltaTime, 0.25f);
		this.accumulator += frameTime;
		final float timeStep = 1 / 60f;
		while (this.accumulator >= timeStep) {
			this.b2World.step(timeStep, 3, 8);
			this.accumulator -= timeStep;
		}
	}

	/*protected static Vector2 calcPlayerDistance() {
		final Vector2 vector = new Vector2();
		vector.x = (playerWake.position.x + playerSleep.position.x) / 2;
		vector.y = (playerWake.position.y + playerSleep.position.y) / 2;
		final double temp1 = Math.pow((playerWake.position.x - playerSleep.position.x), 2);
		final double temp2 = Math.pow((playerWake.position.y - playerSleep.position.y), 2);
		distance = Math.sqrt(temp1 + temp2);
		return vector;
	}*/

	/*protected static double calcPossibleXPlayerDistance() {
		final Vector2 vector = new Vector2();
		int xOffset = 2;
		int yOffset = 2;
		if (playerWake.position.x < playerSleep.position.x) {
			xOffset = -2;
		}
		if (playerWake.position.y < playerSleep.position.y) {
			yOffset = -2;
		}

		vector.x = (playerWake.position.x + xOffset + playerSleep.position.x) / 2;
		vector.y = (playerWake.position.y + yOffset + playerSleep.position.y) / 2;
		final double temp1 = Math.pow(((playerWake.position.x + xOffset) - playerSleep.position.x), 2);
		final double temp2 = Math.pow(((playerWake.position.y + yOffset) - playerSleep.position.y), 2);
		return Math.sqrt(temp1 + temp2);
	}*/

	private void createDynamicObjects() {
		// add objects after step!!
		for (final AbstractGameObject object : this.contactListener.getObjectsToAdd()) {
			// object.setToRender(true);
			// level.addGameObject(object);
			object.initPhysics();
		}
		this.contactListener.getObjectsToAdd().clear();
		// add objects after step!!
		for (final AbstractGameObject object : WorldController.objectsToAdd) {
			// object.setToRender(true);
			// level.addGameObject(object);
			// object.initPhysics();
		}
		WorldController.objectsToAdd.clear();
	}

	/*private void resetGame() {
		this.reset = false;
		this.playerSleep.getBody().setTransform(new Vector2(0.5f, 1.5f), 0);
		this.playerSleep.getBody().setLinearVelocity(new Vector2(0, 0));
		this.playerSleep.getBody().setAngularVelocity(0);
		this.timeElapsed = 0;
		this.coinCount = 0;
		}*/

	/**
	 * Use this method to add dynamic objects. For example if the Player shoots, to add the bullet he shoots. "throwItem(float)"
	 *
	 * @param object
	 */
	public static void addTempGameObject(final AbstractGameObject object) {
		WorldController.objectsToAdd.add(object);
	}

	/**
	 * The World is needed to create bodies and fixtures, for example in the Player
	 *
	 * @return b2World the World
	 */
	public World getB2World() {
		return this.b2World;
	}

	protected long getTimeElapsed() {
		return this.timeElapsed;
	}
}
