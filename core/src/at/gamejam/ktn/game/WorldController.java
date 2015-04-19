package at.gamejam.ktn.game;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequencer;

import at.gamejam.ktn.game.entites.Item;
import at.gamejam.ktn.game.entites.PlayerSleep;
import at.gamejam.ktn.game.entites.PlayerWake;
import at.gamejam.ktn.game.entities.GameObject;
import at.gamejam.ktn.utils.CameraHelper;
import at.gamejam.ktn.utils.Constants;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

public class WorldController {
	public CameraHelper				cameraHelper;
	public PlayerSleep				playerSleep;
	public PlayerWake				playerWake;
	public long						timeElapsed;
	public int						coinCount		= Constants.START_ITEM_COUNT;
	public int						redBullCount	= Constants.START_ITEM_COUNT;
	private World					b2World;
	private GeneratedLevel			level;
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

		this.playerSleep = new PlayerSleep(new Vector2(-1.5f, -1.0f), this);
		this.playerWake = new PlayerWake(new Vector2(1.5f, -1.0f), this);

		// this.cameraHelper.setTarget(this.playerSleep.getB2Body());
		this.cameraHelper.setTarget(new Vector2(this.playerSleep.position.x - this.playerWake.position.x, this.playerSleep.position.y - this.playerWake.position.y));
		// Vector(x2-x1,y2-y1
		// this.level = new Level(this.b2World);

		this.level = new GeneratedLevel(this.b2World);
		this.contactListener = new MyContactListener(this);
		this.b2World.setContactListener(this.contactListener);
		Gdx.input.setInputProcessor(new InputManager(this.playerWake, this.playerSleep, this.cameraHelper));
		// Gdx.audio.newSound(Gdx.files.internal(Constants.MUSIC)).play();
		try {
			Sequencer sequencer = MidiSystem.getSequencer();

			sequencer.setSequence(MidiSystem.getSequence(new FileInputStream(new File(Constants.MUSIC))));

			sequencer.open();
			sequencer.start();
		} catch (InvalidMidiDataException | IOException | MidiUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*midiPlayer.open(Constants.MUSIC);
		midiPlayer.setLooping(true);
		midiPlayer.setVolume(0.5f);
		midiPlayer.play();*/
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

		// this.cameraHelper.update(deltaTime);
		Vector2 vector = new Vector2();
		vector.x = (this.playerWake.position.x + this.playerSleep.position.x) / 2;
		vector.y = (this.playerWake.position.y + this.playerSleep.position.y) / 2;
		this.cameraHelper.update(vector);
		double temp1 = Math.pow((this.playerWake.position.x - this.playerSleep.position.x), 2);
		double temp2 = Math.pow((this.playerWake.position.y - this.playerSleep.position.y), 2);
		double distance = Math.sqrt(temp1 + temp2);
		// System.out.println(distance);
		double factor = 0.2;
		/*if (distance < 5) {
			factor = factor * (-1);
		}*/
		this.cameraHelper.setZoom((float) (distance * factor));
		// System.out.println(distance);

		for (GameObject o : this.level.getGameObjects()) {
			if (o instanceof Item) {
				Item b = (Item) o;
				if (b.isCollected() && !b.destroyed) {
					this.b2World.destroyBody(b.getB2Body());
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

		for (Item item : Item.itemList) {
			if (item.isFlying) {
				item.flyingTime += deltaTime;
			}
			if (item.flyingTime > 1) {
				item.collectable = true;
			}
		}
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

	protected GeneratedLevel getLevel() {
		return this.level;
	}

	/*protected void addAbstractItem(final GameObject object) {
		this.level.addGameObject(object);
	}*/
}
